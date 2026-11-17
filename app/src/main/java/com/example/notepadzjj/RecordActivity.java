package com.example.notepadzjj;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 记录界面：新增时保存到数据库，修改时按 id 更新
 */
public class RecordActivity extends Activity {

    private TextView tvMainTitle;
    private TextView tvBack;
    private TextView tvTime;
    private EditText etTitle;
    private EditText etContent;
    private ImageView ivSave;
    private ImageView ivDelete;

    private MyDatabase myDatabase;
    private long noteId = -1;   // -1 表示新增

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        myDatabase = new MyDatabase(this);

        tvMainTitle = findViewById(R.id.tv_main_title);
        tvBack = findViewById(R.id.tv_back);
        tvTime = findViewById(R.id.tv_time);
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        ivSave = findViewById(R.id.iv_save);
        ivDelete = findViewById(R.id.iv_del);

        noteId = getIntent().getLongExtra("id", -1);

        if (noteId == -1) {
            // 新增
            tvMainTitle.setText("添加记事");
            tvBack.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            ivDelete.setVisibility(View.GONE);
        } else {
            // 修改：回显原内容
            tvMainTitle.setText("修改记事");
            tvBack.setVisibility(View.VISIBLE);
            tvBack.setOnClickListener(v -> finish());

            MyDatabase.Note note = myDatabase.queryById(noteId);
            if (note != null) {
                etTitle.setText(note.title);
                etContent.setText(note.content);
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText("最后修改：" + note.time);
            }
        }

        ivSave.setOnClickListener(v -> save());
        ivDelete.setOnClickListener(v -> confirmDelete());
    }

    /** 保存：新增用 insert，修改用 update */
    private void save() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            Toast.makeText(this, "标题和内容不能都为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(title)) {
            title = "无标题";
        }

        long result;
        if (noteId == -1) {
            result = myDatabase.insert(title, content);
        } else {
            result = myDatabase.update(noteId, title, content);
        }

        if (result > 0 || (noteId != -1 && result == 0)) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    /** 删除：同样先弹确认框 */
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要删除这条记事吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    if (myDatabase.delete(noteId) > 0) {
                        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myDatabase != null) {
            myDatabase.close();
        }
    }
}
