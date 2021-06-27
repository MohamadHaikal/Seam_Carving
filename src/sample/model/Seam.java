package sample.model;


public class Seam {
    private double energy;
    private int[] pixels;
    private String dir;

    public Seam(int size, String dir) {
        pixels = new int[size];
        this.dir = dir;
    }
    String getDir()
    {
        return dir;
    }
    int[] getPixels()
    {
        return pixels;
    }
    void setPixels(int position, int value)
    {
        pixels[position] = value;
    }
    double getEnergy()
    {
        return energy;
    }
    void setEnergy(double energy)
    {
        this.energy = energy;
    }

}
