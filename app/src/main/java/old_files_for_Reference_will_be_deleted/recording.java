package old_files_for_Reference_will_be_deleted;

public class recording {

    private String full_file_name;
    private String recording_title;
    private String interviewee_attribute;
    private String date_attribute;
    /*
    Attributes to add
    -interveiwee
    -date
    -length
    -tags
    -Photo, probably as a file path?
     */


    public recording(String f){
        full_file_name = f;
        this.retreiveData();
        interviewee_attribute = "Unnamed";
        date_attribute = "Undated";
    }

    public recording(String f, String s){
        full_file_name = f;
        recording_title = s;
        interviewee_attribute = "Unnamed";
        date_attribute = "Undated";
    }

    public recording(String f, String s, String i, String d){
        full_file_name = f;
        recording_title = s;
        interviewee_attribute = i;
        date_attribute = d;

    }

    public String getFullFileName(){return full_file_name;}
    public String getRecordingTitle(){ return recording_title;}
    public String getInterviewee() {return interviewee_attribute;}
    public String getDate() {return date_attribute;}

    public void retreiveData(){
        recording_title = full_file_name.substring(full_file_name.lastIndexOf( '/' )+1, full_file_name.lastIndexOf('.'));
    }

}
