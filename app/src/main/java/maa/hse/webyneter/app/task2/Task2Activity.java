/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package maa.hse.webyneter.app.task2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import maa.hse.webyneter.app.R;

public class Task2Activity extends ListActivity {
    private static final int STATUS_SUCCESS = 0;
    private static final int STATUS_CANCELLED = 1;
    private static final int STATUS_NO_STORAGE = 2;
    private static final int STATUS_NOT_LOADED = 3;
    private static final int MENU_ID_RENAME = 1;
    private static final int MENU_ID_REMOVE = 2;
    private static final int DIALOG_RENAME_GESTURE = 1;
    private static final int REQUEST_NEW_GESTURE = 1;
    private static final String GESTURES_INFO_ID = "gestures.info_id";

    private static GestureLibrary gestureLibrary;
    private final File gestureStorageFile = new File(Environment.getExternalStorageDirectory(), "gestures");
    private final Comparator<NamedGesture> namedGestureComparator = new Comparator<NamedGesture>() {
        public int compare(NamedGesture object1, NamedGesture object2) {
            return object1.name.compareTo(object2.name);
        }
    };
    private GesturesAdapter gesturesAdapter;
    private GesturesLoadTask gesturesLoadTask;
    private TextView tvEmpty;
    private Dialog dialog;
    private EditText dlgInput;
    private NamedGesture currentRenameGesture;
    private Button btnListCreate;
    private Button btnListReload;
    private Button btnListRecognize;

    static GestureLibrary getGestureLibrary() {
        return gestureLibrary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2_gestures_list);

        initializeUiVariables();

        gesturesAdapter = new GesturesAdapter(this);
        gesturesAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (gesturesAdapter.getCount() == 0) {
                    btnListReload.setEnabled(false);
                    btnListRecognize.setEnabled(false);
                } else {
                    btnListReload.setEnabled(true);
                    btnListRecognize.setEnabled(true);
                }
                super.onChanged();
            }
        });
        setListAdapter(gesturesAdapter);

        if (gestureLibrary == null) {
            gestureLibrary = GestureLibraries.fromFile(gestureStorageFile);
        }
        tvEmpty = (TextView) findViewById(android.R.id.empty);
        loadOrReloadGestures();

        registerForContextMenu(getListView());
    }

    private void initializeUiVariables() {
        btnListCreate = (Button) findViewById(R.id.task2_btnListCreate);
        btnListReload = (Button) findViewById(R.id.task2_btnListReload);
        btnListRecognize = (Button) findViewById(R.id.task2_btnListRecognize);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void addGesture(View v) {
        Intent intent = new Intent(this, GestureCreationActivity.class);
        startActivityForResult(intent, REQUEST_NEW_GESTURE);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void reloadGestures(View v) {
        loadOrReloadGestures();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void recognizeGesture(View v) {
        Intent intent = new Intent(this, GestureRecognitionActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_NEW_GESTURE:
                    loadOrReloadGestures();
                    break;
            }
        }
    }

    private void loadOrReloadGestures() {
        if (gesturesLoadTask != null
                && gesturesLoadTask.getStatus() != GesturesLoadTask.Status.FINISHED) {
            gesturesLoadTask.cancel(true);
        }
        gesturesLoadTask = (GesturesLoadTask) new GesturesLoadTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (gesturesLoadTask != null
                && gesturesLoadTask.getStatus() != GesturesLoadTask.Status.FINISHED) {
            gesturesLoadTask.cancel(true);
            gesturesLoadTask = null;
        }

        cleanupRenameDialog();
    }

    private void checkForEmpty() {
        if (gesturesAdapter.getCount() == 0) {
            tvEmpty.setText(R.string.gestures_empty);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (currentRenameGesture != null) {
            outState.putLong(GESTURES_INFO_ID, currentRenameGesture.gesture.getID());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        long id = state.getLong(GESTURES_INFO_ID, -1);
        if (id != -1) {
            final Set<String> entries = gestureLibrary.getGestureEntries();
            out:
            for (String name : entries) {
                for (Gesture gesture : gestureLibrary.getGestures(name)) {
                    if (gesture.getID() == id) {
                        currentRenameGesture = new NamedGesture();
                        currentRenameGesture.name = name;
                        currentRenameGesture.gesture = gesture;
                        break out;
                    }
                }
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        final AdapterView.AdapterContextMenuInfo mi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(((TextView) mi.targetView).getText());

        menu.add(0, MENU_ID_RENAME, 0, R.string.gestures_rename);
        menu.add(0, MENU_ID_REMOVE, 0, R.string.gestures_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final NamedGesture gesture = (NamedGesture) menuInfo.targetView.getTag();

        switch (item.getItemId()) {
            case MENU_ID_RENAME:
                renameGesture(gesture);
                return true;
            case MENU_ID_REMOVE:
                deleteGesture(gesture);
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void renameGesture(NamedGesture gesture) {
        currentRenameGesture = gesture;
        showDialog(DIALOG_RENAME_GESTURE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_RENAME_GESTURE) {
            return createRenameDialog();
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        if (id == DIALOG_RENAME_GESTURE) {
            dlgInput.setText(currentRenameGesture.name);
        }
    }

    private Dialog createRenameDialog() {
        final View layout = View.inflate(this, R.layout.activity_task2_dialog_rename, null);
        dlgInput = (EditText) layout.findViewById(R.id.task2_etDialogName);
        ((TextView) layout.findViewById(R.id.task2_lblDialogRename)).setText(R.string.gestures_rename_label);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(0);
        builder.setTitle(getString(R.string.gestures_rename_title));
        builder.setCancelable(true);
        builder.setOnCancelListener(new Dialog.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                cleanupRenameDialog();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel_action),
                new Dialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cleanupRenameDialog();
                    }
                }
        );
        builder.setPositiveButton(getString(R.string.rename_action),
                new Dialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        changeGestureName();
                    }
                }
        );
        builder.setView(layout);
        return builder.create();
    }

    private void changeGestureName() {
        final String name = dlgInput.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            final NamedGesture renameGesture = currentRenameGesture;
            final GesturesAdapter adapter = gesturesAdapter;
            final int count = adapter.getCount();

            // Simple linear search, there should not be enough items to warrant
            // a more sophisticated search
            for (int i = 0; i < count; i++) {
                final NamedGesture gesture = adapter.getItem(i);
                if (gesture.gesture.getID() == renameGesture.gesture.getID()) {
                    gestureLibrary.removeGesture(gesture.name, gesture.gesture);
                    gesture.name = dlgInput.getText().toString();
                    gestureLibrary.addGesture(gesture.name, gesture.gesture);
                    break;
                }
            }

            adapter.notifyDataSetChanged();
        }
        currentRenameGesture = null;
    }

    private void cleanupRenameDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        currentRenameGesture = null;
    }

    private void deleteGesture(NamedGesture gesture) {
        gestureLibrary.removeGesture(gesture.name, gesture.gesture);
        gestureLibrary.save();

        final GesturesAdapter adapter = gesturesAdapter;
        adapter.setNotifyOnChange(false);
        adapter.remove(gesture);
        adapter.sort(namedGestureComparator);
        checkForEmpty();
        adapter.notifyDataSetChanged();

        Toast.makeText(this, R.string.gestures_delete_success, Toast.LENGTH_SHORT).show();
    }

    static class NamedGesture {
        String name;
        Gesture gesture;
    }

    private class GesturesLoadTask extends AsyncTask<Void, NamedGesture, Integer> {
        private int mThumbnailSize;
        private int mThumbnailInset;
        private int mPathColor;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final Resources resources = getResources();
            mPathColor = resources.getColor(R.color.gesture_color);
            mThumbnailInset = (int) resources.getDimension(R.dimen.gesture_thumbnail_inset);
            mThumbnailSize = (int) resources.getDimension(R.dimen.gesture_thumbnail_size);

            btnListCreate.setEnabled(false);
            btnListReload.setEnabled(false);
            btnListRecognize.setEnabled(false);

            gesturesAdapter.setNotifyOnChange(false);
            gesturesAdapter.clear();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (isCancelled()) return STATUS_CANCELLED;
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return STATUS_NO_STORAGE;
            }

            final GestureLibrary store = gestureLibrary;

            if (store.load()) {
                for (String name : store.getGestureEntries()) {
                    if (isCancelled()) break;

                    for (Gesture gesture : store.getGestures(name)) {
                        final Bitmap bitmap = gesture.toBitmap(mThumbnailSize, mThumbnailSize,
                                mThumbnailInset, mPathColor);
                        final NamedGesture namedGesture = new NamedGesture();
                        namedGesture.gesture = gesture;
                        namedGesture.name = name;

                        gesturesAdapter.addBitmap(namedGesture.gesture.getID(), bitmap);
                        publishProgress(namedGesture);
                    }
                }

                return STATUS_SUCCESS;
            }

            return STATUS_NOT_LOADED;
        }

        @Override
        protected void onProgressUpdate(NamedGesture... values) {
            super.onProgressUpdate(values);

            final GesturesAdapter adapter = gesturesAdapter;
            adapter.setNotifyOnChange(false);

            for (NamedGesture gesture : values) {
                adapter.add(gesture);
            }

            adapter.sort(namedGestureComparator);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result == STATUS_NO_STORAGE) {
                getListView().setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText(getString(R.string.gestures_error_loading,
                        gestureStorageFile.getAbsolutePath()));
            } else {
                btnListCreate.setEnabled(true);
                btnListReload.setEnabled(true);
                btnListRecognize.setEnabled(true);
                checkForEmpty();
            }
        }
    }

    private class GesturesAdapter extends ArrayAdapter<NamedGesture> {
        private final LayoutInflater layoutInflater;
        private final Map<Long, Drawable> longThumbnailMap = Collections.synchronizedMap(new HashMap<Long, Drawable>());

        public GesturesAdapter(Context context) {
            super(context, 0);
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        void addBitmap(Long id, Bitmap bitmap) {
            longThumbnailMap.put(id, new BitmapDrawable(bitmap));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.activity_task2_gestures_item, parent, false);
            }

            final NamedGesture gesture = getItem(position);
            final TextView label = (TextView) convertView;

            label.setTag(gesture);
            label.setText(gesture.name);
            label.setCompoundDrawablesWithIntrinsicBounds(longThumbnailMap.get(gesture.gesture.getID()), null, null, null);

            return convertView;
        }
    }
}
