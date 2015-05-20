import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileIO extends Parse {

    public File inputFile;

    public BufferedReader bufferedReader;

    public static final String FILE_EXTENSION = "net";

    public String getInputFileExtension() {
        return FILE_EXTENSION;
    }

    public boolean checkForArguments(String args[]){
        if(args.length == 1){
            return true;
        }
        else{
            System.err.println("No arguments passed!");
            return false;
        }
    }

    public boolean checkFileExtension(File input){
        String extension = "";
        String fileName = input.getName();
        int i = fileName.lastIndexOf('.');
        if (fileName.lastIndexOf('.') > 0) {
            extension = fileName.substring(i+1);
        }
        if(extension.equals(getInputFileExtension())){
            return true;
        }
        else{
            System.err.println("Unsupported Extension");
            return false;
        }
    }


    public void read(File input) {
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(new FileReader(input));
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        this.bufferedReader = bufferedReader;
    }

    public BufferedReader getReader(){
        return bufferedReader;
    }

    public void init(File inputFile) {
        this.inputFile = inputFile;
    }

    public void init(String args[]) {
        if(checkForArguments(args)){
            init(new File(args[0]));
            if(checkFileExtension(inputFile)){
                read(inputFile);
            }
        }
    }
}
