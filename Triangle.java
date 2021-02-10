import java.lang.Math;

class Triangle extends TwoDimensionalShape implements MultiVariantShape
{
    int longestSide, side1, side2, side3;
    private TriangleVariant variant;

    public Triangle(int a, int b, int c){
        side1 = a;
        side2 = b;
        side3 = c;
        if(isFlat(side1, side2, side3)){
            variant = TriangleVariant.FLAT;
        }
        else if(isIllegal(side1, side2, side3)){
            variant = TriangleVariant.ILLEGAL;
        }
        else if(isRightAngled(side1, side2, side3)){
            variant = TriangleVariant.RIGHT;
        }
        else if(isEquilateral(side1, side2, side3)){
            variant = TriangleVariant.EQUILATERAL;
        }
        else if(isIsoceles(side1, side2, side3)){
            variant = TriangleVariant.ISOSCELES;
        }
        else if(isScalene(side1, side2, side3)){
            variant = TriangleVariant.SCALENE;
        }
        else{
            variant = TriangleVariant.IMPOSSIBLE;
        }
    }

    public TriangleVariant getVariant(){
        return variant;
    }

    private boolean isFlat(int a, int b, int c){
        int longest = getLongestSide();
        if(longest == a){
            if(longest == b + c){
                return true;
            }
        }
        else if(longest == b){
            if(longest == a + c){
                return true;
            }
        }
        else if(longest == c){
            if(longest == b + a){
                return true;
            }
        }
        return false;
    }

    private boolean isRightAngled(int a, int b, int c){
        if((a * a) + (b * b) == (c * c)){
            return true;
        }
        return false;
    }

    private boolean isIllegal(int a, int b, int c){
        if(a + b <= c || a + c <= b || b + c <= a){
            return true;
        }
        return false;
    }

    private boolean isScalene(int a, int b, int c){
        if(a != b || b != c || c != a){
            return true;
        }
        return false;
    }

    private boolean isIsoceles(int a, int b, int c){
        if(a == b || b == c || c == a){
            return true;
        }
        return false;
    }

    private boolean isEquilateral(int a, int b, int c){
        if(a == b && b == c){
            return true;
        }
        return false;
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
        double area, s;
        s = (side1 + side2 + side3);
        s /= 2;
        area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
        return area;
    }

    public int calculatePerimeterLength() {
        int perimeter = side1 + side2 + side3;
        return perimeter;
    }
}