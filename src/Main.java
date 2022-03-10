import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class Main
{
    public static void reportBundleAnalys(Matrix m)
    {
        System.out.print("Имя файла");
        for(int i=0;i<m.getWidth()-1;i++)
            System.out.print("\tМетод №"+(i+1));
        System.out.print("\tИТОГ\n");

        for(int doc=0;doc<m.getHeight();doc++)
        {
            Bundle bundle = (Bundle) m.getRows()[doc].getObj();
            System.out.print(bundle.report.getFilename());
            System.out.print("\t");

            for(Float res:m.getRows()[doc].getCortege())
                System.out.print(res+"\t");
            System.out.println();
        }
    }

    public static void main(String args[])
    {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("config.txt"),"utf-8")))
        {
            //Читаем конфиг файл со списком ворд файлов
            ArrayList<StringBuffer> conf = new ArrayList<StringBuffer>();
            int code = 0;
            char sym;
            int counter = 0;


            while(in.ready())
            {
                conf.add(new StringBuffer(in.readLine()));
            }

            long initialization = System.currentTimeMillis();

            ArrayList<Bundle> bundles = new ArrayList<Bundle>();
            WordParser wordParser = new WordParser();

            for(Iterator<StringBuffer> i = conf.iterator(); i.hasNext();)
            {
                StringBuffer fileName = i.next();
                Bundle bb = new Bundle();
                try
                {
                    bb.setReport(wordParser.parseDoc(fileName.toString()));
                    bundles.add(bb);
                }
                catch(Exception e)
                {
                    System.out.println(e.toString());
                }
            }

            Bundle toCompare = bundles.get(0);
            bundles.remove(0);
            Matrix m = new BuilderWords(toCompare,bundles).buildMatrix();

            System.out.println("Время чтения word + инициализации = "+(System.currentTimeMillis()-initialization)+" [мс]");

            GroupVoters groupVoters = new GroupVoters(m);

            long t1 = System.currentTimeMillis();
                reportBundleAnalys(groupVoters.verdict());
            long t2 = System.currentTimeMillis();
            System.out.print("t(алгоритма сравнения) = ");
            System.out.print(t2-t1);
            System.out.println(" [мс]");


            BuilderMeta builderMeta = new BuilderMeta(toCompare,bundles);
            groupVoters.setM(builderMeta.buildMatrix());
            reportBundleAnalys(groupVoters.verdict());
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
