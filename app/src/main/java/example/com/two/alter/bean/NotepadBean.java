package example.com.two.alter.bean;
public class NotepadBean {
    private String id;                  //记录的id
    private String notepadContent;   //记录的内容
    private String notepadTime;       //保存记录的时间
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNotepadContent() {
        return notepadContent;
    }
    public void setNotepadContent(String notepadContent) {
        this.notepadContent = notepadContent;
    }
    public String getNotepadTime() {
        return notepadTime;
    }

    public NotepadBean() {
    }

    @Override
    public String toString() {
        return "NotepadBean{" +
                "id='" + id + '\'' +
                ", notepadContent='" + notepadContent + '\'' +
                ", notepadTime='" + notepadTime + '\'' +
                '}';
    }

    public NotepadBean(String id, String notepadContent, String notepadTime) {
        this.id = id;
        this.notepadContent = notepadContent;
        this.notepadTime = notepadTime;
    }

    public void setNotepadTime(String notepadTime) {
        this.notepadTime = notepadTime;
    }
}
