package net.lanoda.app.android.apihelpers;

/**
 * Created by isaac on 9/17/2016.
 */
public class ApiFilter {

    public static enum Operators {
        EqualTo,
        GreaterThan,
        LessThan,
        IContains,
        Contains,
        NotEqualTo,
    }

    public String Property;
    public Operators Operator;
    public String Value;

}
