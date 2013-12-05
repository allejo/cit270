/*
    Vladimir Jimenez
    License: MIT
 */

package PostFix;

import java.io.*;
import java.util.Scanner;

public class FileEditor
{
    private File   file;
    private String filePath;
    private String fileParent;
    private String fileName;
    private String fileNameWithoutExtension;
    private String extension;
    private long   size;
    private int    lineCount;

    /**
     * Creates an object that will handle all the text file manipulation
     *
     * @param pathToFile The path to the file we will be manipulating
     *
     * @throws IOException
     */
    public FileEditor (String pathToFile) throws IOException
    {
        this(new File(pathToFile));
    }

    /**
     * Creates an object that will handle all the text file manipulation
     *
     * @param _file The file we will be modifying
     */
    public FileEditor (File _file) throws IOException
    {
        if (!_file.exists())
        {
            throw new FileNotFoundException();
        }

        file       = _file;
        filePath   = _file.getCanonicalPath();
        fileParent = _file.getCanonicalFile().getParent() + File.separator;
        fileName   = _file.getName();
        size       = _file.length();
        lineCount  = countLines();

        if (!_file.isDirectory() && fileName.lastIndexOf(".") >= 0)
        {
            fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        else
        {
            fileNameWithoutExtension = fileName;
        }
    }

    /**
     * Counts the total number of lines that the specified file contains
     *
     * @return The total number of lines
     */
    private int countLines ()
    {
        LineNumberReader lnr = null;

        try
        {
            lnr = new LineNumberReader(new FileReader(new File(filePath)));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File '" + filePath + "' not found.");
        }

        if (lnr != null)
        {
            try
            {
                lnr.skip(Long.MAX_VALUE);
            }
            catch (IOException e)
            {
                System.out.println("An IOException has occurred while getting the total line count.");
            }
        }

        return lnr.getLineNumber();
    }

    /**
     * Creates a new file if the file does not exist and returns the main.FileEditor object of it
     *
     * @param filePath The location of where the file is to be created
     *
     * @return A FileEditor object
     *
     * @throws IOException
     */
    public static FileEditor createFile (String filePath) throws IOException
    {
        File _myFile = new File(filePath);
        _myFile.createNewFile();

        return new FileEditor(_myFile.getCanonicalPath());
    }

    /**
     * Modify the contents of a text file by rewriting a line
     *
     * @param mode              'add', 'edit', or 'remove'
     * @param lineNumber        The line number we will be editing
     * @param textToAddOrRemove The text we will be writing to the file or NULL if removing a line
     */
    public void edit (String mode, int lineNumber, String textToAddOrRemove) throws IOException
    {
        int myLineNumber = 0; //The line number of the file being read
        String line; //The line that is currently being read
        FileEditor newFile = createFile(filePath + "_new");

        Scanner myFile = new Scanner(new File(filePath)); //Open the file

        while (myFile.hasNext()) //Read through the file
        {
            line = myFile.nextLine(); //Store the line of text
            myLineNumber++; //Increase the line number

            if (mode.equals("add")) //Check the mode
            {
                newFile.writeToFile(line);

                if (myLineNumber == lineNumber) //If the line number is equal
                {
                    newFile.writeToFile(textToAddOrRemove); //Write the new line after
                }
            }
            else if (mode.equals("edit"))
            {
                if (myLineNumber == lineNumber)
                {
                    newFile.writeToFile(textToAddOrRemove); //Write the new line instead of the existing line
                }
                else //Continue writing the existing lines
                {
                    newFile.writeToFile(line);
                }
            }
            else if (mode.equals("remove"))
            {
                if (myLineNumber != lineNumber) //Continue writing the existing lines
                {
                    newFile.writeToFile(line);
                }
            }
            else //Catch any errors with the mode
            {
                return;
            }
        }

        myFile.close(); //Close the file
        System.gc();

        new File(filePath).delete(); //Delete the old file
        new File(newFile.filePath).renameTo(new File(filePath)); //Rename the new file to the same name as the old one
    }

    /**
     * @return The extension of the file
     */
    public String get_extension ()
    {
        return this.extension;
    }

    /**
     * @return The file object
     */
    public File get_file ()
    {
        return this.file;
    }

    /**
     * @return Return the path to the file
     */
    public String get_file_path ()
    {
        return this.filePath;
    }

    /**
     * @return Return the path to the parent of the file
     */
    public String get_file_parent ()
    {
        return this.fileParent;
    }

    /**
     * @return The file name with the extension
     */
    public String get_file_name ()
    {
        return this.fileName;
    }

    /**
     * @return The file name without the extension
     */
    public String get_file_name_wo_extension ()
    {
        return this.fileNameWithoutExtension;
    }

    /**
     * Returns the total number of lines a file has
     *
     * @return The total amount of lines
     */
    public int get_line_count ()
    {
        return this.lineCount;
    }

    /**
     * @return The file name without the extension
     */
    public String get_size ()
    {
        return String.valueOf(this.size);
    }

    /**
     * Get the content found on line X of the file
     *
     * @param lineNumber The line number we will return
     *
     * @return The contents of the line specified
     */
    public String returnLine (int lineNumber) throws FileNotFoundException
    {
        int _lineCount = 0; //Keep track of the line number
        String line; //A place to store the current line being read

        Scanner myFile = new Scanner(new File(filePath)); //Open the file

        while (myFile.hasNext()) //Read each line of the text file
        {
            line = myFile.nextLine(); //Store the current line for easy access
            _lineCount++; //Increase the line count

            if (lineNumber == _lineCount) //If the line equals the parameter, return the line number
            {
                return line;
            }
        }

        myFile.close(); //Close the file
        return ""; //Line not found
    }

    /**
     * Write the specified text to the end of the file (adds a new line after the text is written)
     *
     * @param textLine The line of text to be written
     *
     * @throws IOException
     */
    public void writeToFile (String textLine) throws IOException
    {
        FileWriter write = new FileWriter(file, true);
        PrintWriter print_line = new PrintWriter(write);

        print_line.printf("%s" + "\n", textLine); //Write the text to the file with a new line at the end
        print_line.close(); //Close the file
        write.close();
    }
}