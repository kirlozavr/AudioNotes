package com.example.audionotes.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.audionotes.database.DataBaseManager;

public class DeleteDialogFragment extends DialogFragment {

    private Context context;
    private long idNote;
    private String titleNote;
    private OnDeleteListener onDeleteListener;
    private DataBaseManager dataBaseManager;

    private DialogInterface.OnClickListener onPositiveClickListener =
            new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dataBaseManager = new DataBaseManager(context);
            dataBaseManager.open();
            dataBaseManager.deleteEntity(idNote);
            dataBaseManager.close();
            if(onDeleteListener != null) {
                onDeleteListener.onDelete();
            }
            dialogInterface.cancel();
        }
    };

    public DeleteDialogFragment(){
    }

    public DeleteDialogFragment(Context context, long idNote, String titleNote){
        this.context = context;
        this.idNote = idNote;
        this.titleNote = titleNote;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Вы хотите удалить заметку: " + titleNote + " ?");
        builder.setPositiveButton("Да", onPositiveClickListener);
        builder.setNegativeButton("Нет", (dialogInterface, i) -> dialogInterface.cancel());
        return builder.create();
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }
}
