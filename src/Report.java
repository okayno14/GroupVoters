import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Report implements Similarity
{
    private ArrayList<String> text;
    private HashMap<String, Integer> textVector;
    private String filename;

    //инициализируется в конструкторе
    private int symCount=0;
    private int symCountNoSpace=0;

    //инициализируется во время подготовки
    private int uniqueWords=0;
    private int wordCount=0;

    private boolean isSetCompatible=true;

    private void init(String filename)
    {
        text = new ArrayList<String>();
        textVector = new HashMap<String, Integer>();
        this.filename=filename;
    }

    public void fillTextVector(String textStr)
    {
        if(textVector.size()>0)
            return;
        //Поиск слов в документе
        Pattern pattern = Pattern.compile("[а-яА-Я0-9ёa-zA-Z-]+");
        Matcher matcher = pattern.matcher(textStr);

        //Слова, найденные регулярным выражением добавляются в коллекцию
        while(matcher.find())
            text.add(matcher.group());

        symCount=textStr.length();
        textStr = textStr.replaceAll(" ","");
        symCountNoSpace=textStr.length();

        HashSet<String> words = new HashSet<String>();
        words.addAll(text);

        for (Iterator<String> i=words.iterator(); i.hasNext(); )
        {
            String w=i.next();
            int buf=wordCount(w);
            wordCount+=buf;
            textVector.put(w,buf);
        }
        uniqueWords=textVector.keySet().size();
    }

    public Report(String textStr, String filename)
    {
        init(filename);
        fillTextVector(textStr);
    }

    private int wordCount(String word)
    {
        int res=0;
        String w = new String();
        for(Iterator<String> i=text.iterator(); i.hasNext();)
        {
            w=i.next();
            if (w.equalsIgnoreCase(word))
                res++;
        }
        return res;
    }

    @Override
    public boolean isSetCompatible() {
        return isSetCompatible;
    }

    public void setSetCompatible(boolean setCompatible) {
        isSetCompatible = setCompatible;
    }

    @Override
    public Set<Object> getSet()
    {
        return new HashSet<Object>(textVector.keySet());
    }

    public ArrayList<String> getText() {
        return text;
    }



    public HashMap<String, Integer> getTextVector() {

        return textVector;
    }

    public String getFilename() {

        return filename;
    }

    public int getSymCount() {

        return symCount;
    }

    public int getUniqueWords() {

        return uniqueWords;
    }

    public int getWordCount() {

        return wordCount;
    }

    public int getSymCountNoSpace() {

        return symCountNoSpace;
    }




}
