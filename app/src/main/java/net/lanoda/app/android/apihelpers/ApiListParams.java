package net.lanoda.app.android.apihelpers;

import java.util.List;

/**
 * Created by isaac on 9/17/2016.
 */
public class ApiListParams {

    public List<ApiFilter> Filters;
    public List<String> Sorts;
    public int PageIndex;
    public int PageSize;

    public String GetFilterString() {
        String finalString = "";

        if (Filters == null) {
            return finalString;
        }

        for(int i = 0; i < Filters.size(); i++) {
            ApiFilter.Operators operator = Filters.get(i).Operator;

            String prop = Filters.get(i).Property;
            String oper = null;
            String valu = Filters.get(i).Value;

            switch(operator) {
                case GreaterThan:
                    oper = "gt";
                    break;
                case LessThan:
                    oper = "lt";
                    break;
                case IContains:
                    oper = "il";
                    break;
                case Contains:
                    oper = "lk";
                    break;
                case NotEqualTo:
                    oper = "ne";
                    break;
                case EqualTo:
                default:
                    oper = "eq";
                    break;
            }

            finalString += prop + "~" + oper + "~" + valu;
            if (i < Filters.size() - 1) {
                finalString += "|";
            }
        }

        return finalString;
    }

    public String GetSortString() {
        String finalString = "";

        if (Sorts == null) {
            return finalString;
        }

        for (int i = 0; i < Sorts.size(); i++) {
            finalString += Sorts.get(i);
            if (i < Sorts.size() - 1) {
                finalString += "|";
            }
        }

        return finalString;
    }
}
