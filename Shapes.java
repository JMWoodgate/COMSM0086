class Shapes
{
    public static void main(String[] args){

        Triangle myTriangle = new Triangle(2, 3, 13);
        long longestSide = myTriangle.getLongestSide();
        System.out.println(myTriangle.toString());

        if(myTriangle instanceof MultiVariantShape) {
            System.out.println("This shape has multiple variants");
        }
        else {
            System.out.println("This shape has only one variant");
        }

        System.out.println(myTriangle);
        System.out.println("Area: " + myTriangle.calculateArea());
        System.out.println("Perimeter: " + myTriangle.calculatePerimeterLength());
        System.out.println(myTriangle.getVariant());

        Rectangle myRectangle = new Rectangle(4, 7);
        myRectangle.calculateArea();
        System.out.println(myRectangle);

        Circle myCircle = new Circle(8);
        myCircle.calculateArea();
        System.out.println(myCircle);

        Colour firstColour = Colour.RED;
        Colour secondColour = Colour.YELLOW;

        TwoDimensionalShape myShape = new Triangle(9, 11, 10);
        System.out.println(myShape);

        myShape = new Rectangle(13, 12);
        myShape.setColour(firstColour);
        System.out.println(myShape.getColour());

        myShape = new Circle(14);
        myShape.setColour(secondColour);
        System.out.println(myShape.getColour());

        TwoDimensionalShape[] shapeArray = new TwoDimensionalShape[100];
        int cnt = 0;
        for (TwoDimensionalShape shape : shapeArray){
            double ranNum = Math.random();
            if(ranNum <= 0.3){
                shape = new Triangle(3, 2, 5);
                cnt++;
            }
            else if(ranNum <= 0.6){
                shape = new Rectangle(4, 6);
            }
           else if(ranNum <= 1.0){
                shape = new Circle(2);
            }
            System.out.println(shape);
        }
        System.out.println(cnt + " triangles");
    }
}