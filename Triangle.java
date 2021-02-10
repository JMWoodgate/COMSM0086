import java.lang.Math;

class Triangle extends TwoDimensionalShape implements MultiVariantShape
{
    long longestSide, side1, side2, side3;
    private TriangleVariant variant;

    public Triangle(long a, long b, long c){
        side1 = a;
        side2 = b;
        side3 = c;
        if(side1 == 0 || side2 == 0 || side3 == 0){
            variant = TriangleVariant.ILLEGAL;
        }
        else if(isRightAngled(side1, side2, side3)){
            variant = TriangleVariant.RIGHT;
        }
        else if(isFlat(side1, side2, side3)){
            variant = TriangleVariant.FLAT;
        }
        else if(isIllegal(side1, side2, side3)){
            variant = TriangleVariant.ILLEGAL;
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

    private boolean isFlat(long a, long b, long c){
        long longest = getLongestSide();
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

    private boolean isRightAngled(long a, long b, long c){
        if((a * a) + (b * b) == (c * c)){
            return true;
        }
        else if((c * c) + (b * b) == (a * a)){
            return true;
        }
        else if((c * c) + (a * a) == (b * b)){
            return true;
        }
        return false;
    }

    private boolean isIllegal(long a, long b, long c){
        if(a + b <= c || a + c <= b || b + c <= a){
            return true;
        }
        return false;
    }

    private boolean isScalene(long a, long b, long c){
        if(a != b || b != c || c != a){
            return true;
        }
        return false;
    }

    private boolean isIsoceles(long a, long b, long c){
        if(a == b || b == c || c == a){
            return true;
        }
        return false;
    }

    private boolean isEquilateral(long a, long b, long c){
        if(a == b && b == c){
            return true;
        }
        return false;
    }

    public long getLongestSide(){
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
        int perimeter = (int)side1 + (int)side2 + (int)side3;
        return perimeter;
    }
}