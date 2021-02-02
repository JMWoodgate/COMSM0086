abstract class TwoDimensionalShape {

    public TwoDimensionalShape() {
    }

    abstract double calculateArea();
    abstract int calculatePerimeterLength();

    private Colour colour;

    Colour getColour(){
        return colour;
    }
    void setColour(Colour newColour){
        colour = newColour;
    }
}