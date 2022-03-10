import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

public class BuilderWords implements BuilderMatrix
{
    private ArrayList<String> header;
    private Bundle toCompare;
    private ArrayList<Bundle> bundles;
    private int width, height;
    private Row[] rows;
    private Hashtable<String, Float> idf;

    public BuilderWords(Bundle toCompare, Bundle bundles[])
    {
        this.bundles = new ArrayList<Bundle>();

        for(Bundle bundle:bundles)
            this.bundles.add(bundle);

        this.toCompare = toCompare;

        initHeader();

        width = header.size();
        height = this.bundles.size();
        rows = new Row[height];
    }

    public BuilderWords(Bundle toCompare, ArrayList<Bundle> bundles)
    {
        this.toCompare = toCompare;
        this.bundles = bundles;

        initHeader();
        width = header.size();
        height = bundles.size();
        rows = new Row[height];

        toCompare.getReport().setSetCompatible(true);
    }

    private void initHeader()
    {
        header = new ArrayList<String>();

        //Зполняем множество словами из документов
        HashSet<String> words = new HashSet<String>();

        Iterator<Bundle> i = bundles.iterator();
        while(i.hasNext())
        {
            Bundle bundle = i.next();
            words.addAll(bundle.report.getText());
        }
        words.addAll(toCompare.report.getText());
        header = new ArrayList<String>(words);
    }

    private int getDocsWithWord(String word)
    {
        Iterator<Bundle> i = bundles.iterator();
        int res=0;
        while(i.hasNext())
        {
            Bundle bundle = i.next();
            if(bundle.report.getTextVector().containsKey(word))
                res++;
        }
        if(toCompare.report.getTextVector().containsKey(word))
            res++;
        return  res;
    }

    private Float getTF_IDF(String word, Report report)
    {
        int buf=0;
        if(report.getTextVector().containsKey(word))
            buf=report.getTextVector().get(word);
        Float tf = (buf / (float) report.getText().size());
        return tf*idf.get(word);
    }

    private Row buildRow(Bundle bundle)
    {
        Float cortege[] = new Float[width];
        int j=0;
        for (String word: header)
            cortege[j++]=getTF_IDF(word,bundle.report);
        return new Row(bundle,cortege);
    }

    @Override
    public Matrix buildMatrix()
    {
        //idf - характеристика слова. Проще будет посчитать один раз и
        //читать из памяти
        idf = new Hashtable<String, Float>();
        for (String word: header)
        {
            Double score = ((height+1) / (double) getDocsWithWord(word));
            score = Math.log10(score);
            idf.put(word,score.floatValue());
        }

        Iterator<Bundle> iter = bundles.iterator();
        int i=0;
        while(iter.hasNext())
        {
            Bundle bundle = iter.next();
            rows[i++]=buildRow(bundle);
        }

        return new Matrix(width,height,buildRow(toCompare),rows);
    }


}
