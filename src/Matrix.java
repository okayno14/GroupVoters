public class Matrix
{
    private Row rows[];
    private int width;
    private int height;
    private Row toCompare;

    public Matrix(int width, int height, Row toCompare, Row rows[])
    {
        this.width = width;
        this.height = height;
        this.toCompare = toCompare;
        this.rows = rows;
    }

    public void sort(int col)
    {
        if(col>=width)
            return;

    }

    public Row[] getRows() {
        return rows;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Row getToCompare() {
        return toCompare;
    }
}
