package helpers;
import model.questions.*;
import com.github.julman99.gsonfire.GsonFireBuilder;
import com.google.gson.*;


/**
 * A singleton JSON helper class.
 *
 * Gson-fire used to properly pass question subclasses.
 * Originally created by https://github.com/julman99/gson-fire
 * Template from: https://sites.google.com/site/gson/gson-user-guide
 */
public class JSONHelper {

    private static Gson gson = null;

    protected JSONHelper()
    {

    }

    public static Gson getInstance()
    {
        if (gson == null)
        {
            gson = createInstance();
        }

        return gson;
    }

    private static Gson createInstance()
    {
        GsonFireBuilder builder = new GsonFireBuilder().registerTypeSelector(Question.class, readElement -> {
            int type = readElement.getAsJsonObject().get("type").getAsInt();

            switch (type) {
                case 0:
                    return RangeQuestion.class;
                case 1:
                    return MultipleChoiceQuestion.class;
                case 2:
                    return YesNoQuestion.class;
                case 3:
                    return FreeTextQuestion.class;
                case 4:
                    return SingleChoiceQuestion.class;
                case 5:
                    return RankQuestion.class;
                default:
                    return null;
            }
        });
        return builder.createGson();
    }
}
