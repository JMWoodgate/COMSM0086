
class Triangle extends TwoDimensionalShape
{
    int longestSide, side1, side2, side3;


    public Triangle(int a, int b, int c){
        side1 = a;
        side2 = b;
        side3 = c;
    }

    public int getLongestSide(){
        if(side1 >= side2 && side1 >= side3){
            longestSide = side1;
        }
        else if(side2 >= side1 && side2 >= side3){
            longestSide = side2;
        }
        else if(side3 >= side1 && side3 >= side2){
            longestSide = side3;
        }
        return longestSide;
    }

    public String toString(){
        return "The longest side of the triangle is " + longestSide;
    }

    public double calculateArea() {
        int area, s;
        s = (side1 * side2 * side3) / 2;
        area = sqrt(s * side1 * side2 * side3);
        return area;
    }

    public int calculatePerimeterLength() {
        int perimeter = side1 + side2 + side3;
        return perimeter;
    }
}