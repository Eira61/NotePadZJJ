package com.example.notepadzjj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 记事本首页：完成查询（列表展示）、跳转新增、点击修改、长按删除
 */
public class MainActivity extends Activity implements NoteAdapter.OnNoteListener {

    private RecyclerView rvList;
    private ImageView ivAdd;
    private TextView tvMainTitle;
    private TextView tvBack;

    private NoteAdapter adapter;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase = new MyDatabase(this);

        // 初始化组件
        rvList = findViewById(R.id.rv_list);
        ivAdd = findViewById(R.id.iv_add);
        tvMainTitle = findViewById(R.id.tv_main_title);
        tvBack = findViewById(R.id.tv_back);

        tvMainTitle.setText("记事本");
        tvBack.setVisibility(View.GONE);

        // 列表
        adapter = new NoteAdapter();
        adapter.setOnNoteListener(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);

        // 点击“添加”按钮，跳转到添加记录界面
        ivAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecordActivity.class);
            startActivity(intent);
        });
    }

    /** 每次回到首页都重新查询数据库，保证列表是最新的 */
    @Override
    protected void onResume() {
        super.onResume();
        queryNotes();
    }

    /** 查询功能：把 note 表里的全部记录读出来显示到列表 */
    private void queryNotes() {
        List<MyDatabase.Note> notes = myDatabase.queryAll();
        adapter.setData(notes);
        if (notes.isEmpty()) {
            Toast.makeText(this, "还没有记事，点右下角＋添加", Toast.LENGTH_SHORT).show();
        }
    }

    /** 点击列表项：跳转到修改记录界面，并把 id 传过去 */
    @Override
    public void onNoteClick(MyDatabase.Note note) {
        Intent intent = new Intent(this, RecordActivity.class);
        intent.putExtra("id", note.id);
        startActivity(intent);
    }

    /** 长按列表项：弹出是否删除的提示对话框 */
    @Override
    public void onNoteLongClick(MyDatabase.Note note) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要删除“" + note.title + "”这条记事吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    int rows = myDatabase.delete(note.id);
                    if (rows > 0) {
                        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        queryNotes();
                    } else {
                        Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
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
