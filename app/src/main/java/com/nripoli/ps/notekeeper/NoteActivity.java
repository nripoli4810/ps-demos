package com.nripoli.ps.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity
{
    public static final String NOTE_POSITION = "com.nripoli.ps.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean isNewNote;
    private Spinner _courseSpinner;
    private EditText _textNoteTitle;
    private EditText _textNoteText;
    private int _notePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _courseSpinner = findViewById(R.id.spinner_courses);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);

        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _courseSpinner.setAdapter(adapterCourses);

        readDisplayStateValues();

        _textNoteTitle = findViewById(R.id.text_note_title);
        _textNoteText = findViewById(R.id.text_note_text);

        if(!isNewNote)
        {
            displayNote();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        saveNote();
    }

    private void saveNote()
    {
        mNote.setCourse((CourseInfo)_courseSpinner.getSelectedItem());
        mNote.setText(_textNoteText.getText().toString());
        mNote.setTitle(_textNoteTitle.getText().toString());
    }

    private void displayNote()
    {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIdx = courses.indexOf(mNote.getCourse());

        _courseSpinner.setSelection(courseIdx);
        _textNoteTitle.setText(mNote.getTitle());
        _textNoteText.setText(mNote.getText());
    }

    private void readDisplayStateValues()
    {
        Intent intent = getIntent();
        _notePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        isNewNote = _notePosition == POSITION_NOT_SET;

        if (isNewNote)
        {
            DataManager dm = DataManager.getInstance();
            _notePosition = dm.createNewNote();
//            mNote = dm.getNotes().get(_notePosition);
        }

        mNote = DataManager.getInstance().getNotes().get(_notePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_send_mail)
        {
            sendEmail();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendEmail()
    {
        CourseInfo course = (CourseInfo)_courseSpinner.getSelectedItem();
        String subject = _textNoteTitle.getText().toString();
        String text = "Checkout what I learned in \"" + course.getTitle() + "\"\n" + _textNoteText.getText().toString();

        // actions are not necessarily one to one
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");

        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        // implicit
        startActivity(intent);
    }
}
