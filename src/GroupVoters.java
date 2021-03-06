import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupVoters
{
    private Matrix m;
    private Similarity similarity;

    public GroupVoters(Matrix m)
    {
        this.m=m;
    }

    private Row cosineSimilarity()
    {
        Float res[] = new Float[m.getHeight()];
        float a_l = m.getToCompare().length();
        for(int i=0;i<m.getHeight();i++)
        {
            Row b = m.getRows()[i];
            float b_l = b.length();
            float scalarPr = m.getToCompare().scalarProduct(b);

            res[i] = scalarPr/(a_l*b_l);
        }
        return new Row(null,res);
    }

    private Row pearsonBase(Row a, Row rows[])
    {
        Float res[] = new Float[rows.length];
        float a_avg = a.avg();
        float numerator=0;
        float denumerator1=0;
        float denumerator2=0;
        for(int i=0;i<rows.length;i++)
        {
            Row b = rows[i];
            float b_avg = b.avg();

            for(int j=0;j<b.getCortege().length;j++)
            {
                float a_deviation=a.getCortege()[j]-a_avg;
                float b_deviaton=b.getCortege()[j]-b_avg;

                numerator+=(a_deviation)*(b_deviaton);
                denumerator1+=a_deviation*a_deviation;
                denumerator2+=b_deviaton*b_deviaton;
            }
            res[i]=numerator/
                    (float)(Math.sqrt((double) denumerator1)*Math.sqrt((double)denumerator2));
        }
        return new Row(null, res);
    }

    private Row pearson()
    {
        return pearsonBase(m.getToCompare(),m.getRows());
    }

    private Row jaccart()
    {
        Object a = m.getToCompare().getObj();
        similarity = (Similarity) a;
        if(!similarity.isSetCompatible())
            return null;
        Set<Object> aSet = similarity.getSet();
        Float res[] = new Float[m.getHeight()];

        for(int i=0; i<m.getHeight(); i++)
        {
            similarity = (Similarity) m.getRows()[i].getObj();
            Set<Object> bSet = similarity.getSet();

            HashSet<Object> buf = new HashSet<>(aSet);
            buf.retainAll(bSet);

            res[i]=(float)buf.size();
            buf.addAll(bSet);
            buf.addAll(aSet);
            res[i] = res[i]/buf.size();
        }
        return new Row(null,res);
    }

    private Row spearman()
    {
        Row aRanked = new Row(m.getToCompare());
        aRanked.rank();

        Row rowsRanked[] = new Row[m.getHeight()];
        for (int i=0;i<rowsRanked.length;i++)
        {
            rowsRanked[i]=new Row(m.getRows()[i]);
            rowsRanked[i].rank();
        }

        return pearsonBase(aRanked,rowsRanked);
    }


    public Matrix verdict(Matrix m)
    {
        this.m=m;
        return verdict();
    }

    public Matrix verdict()
    {
        ArrayList<Row> votes = new ArrayList<Row>();

        votes.add(cosineSimilarity());
        votes.add(pearson());

        Row jaccart = jaccart();
        if(jaccart!=null)
            votes.add(jaccart);
        votes.add(spearman());

        Row rows[] = new Row[m.getHeight()];
        for(int i=0;i<rows.length;i++)
        {
            Float cortege[] = new Float[votes.size()+1];
            int j=0;
            int sum=0;
            for(Iterator<Row> iterator = votes.iterator(); iterator.hasNext();)
            {

                cortege[j++]=iterator.next().getCortege()[i];
                sum+=cortege[j-1];
            }
            cortege[j++]=sum/(float)votes.size();
            rows[i]=new Row(m.getRows()[i].getObj(),cortege);
        }
        return new Matrix(votes.size()+1,m.getHeight(),m.getToCompare(),rows);
    }

    public void setM(Matrix m) {
        this.m = m;
    }
}
