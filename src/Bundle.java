import java.util.Set;

public class Bundle implements Similarity
{
    private String fileName;
    private int order;
    Report report;

    public Bundle()
    {
        order = (int) Math.random()*10000+100;
        fileName= new Integer(order).toString();
    }

    public Bundle(Report report)
    {
        this.report=report;
        order = (int) Math.random()*10000+100;
        fileName= new Integer(order).toString();
    }

    public String getFileName() {
        return fileName;
    }

    public int getOrder() {
        return order;
    }

    public Report getReport() {
        return report;
    }

    @Override
    public boolean isSetCompatible() {
        return report.isSetCompatible();
    }

    @Override
    public Set<Object> getSet() {
        return report.getSet();
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
