/*
    Vladimir Jimenez
    License: MIT
 */

package PostFix;

import java.io.File;

public class Main
{
    public static void main (String[] args)
    {
        PostFixEvaluator test = new PostFixEvaluator(new File("C:\\Users\\allejo\\Downloads\\PostFix\\src\\sample"));
        test.printToFile();
    }
}
