class Shapes
{

    public static void main(String[] args){

        Triangle myTriangle = new Triangle(2, 5, 3);
        int longestSide = myTriangle.getLongestSide();
        System.out.println(myTriangle.toString());

        myTriangle.calculateArea();
        System.out.println(myTriangle);

        Rectangle myRectangle = new Rectangle(4, 7);
        myRectangle.calculateArea();
        System.out.println(myRectangle);

        Circle myCircle = new Circle(8);
        myCircle.calculateArea();
        System.out.println(myCircle);

        TwoDimensionalShape myShape = new Triangle(9, 11, 10);
        System.out.println(myShape);

        myShape = new Rectangle(13, 12);
        System.out.println(myShape);

        myShape = new Circle(14);
        System.out.println(myShape);
    }
}