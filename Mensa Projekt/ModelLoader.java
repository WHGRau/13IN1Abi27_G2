 


/**
 * Write a description of class ModelLoader here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public  class ModelLoader
{
    // instance variables - replace the example below with your own
    private static Login model = new Login();

    /**
     * Constructor for objects of class ModelLoader
     */
    public ModelLoader()
    {
        // initialise instance variables
        model = new Login();
    }
    
    public static Login getModel(){
        return model;
    }

}
