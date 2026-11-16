record Point(int x, int y){}

String describe(Object o){
    return switch(o){
        case Point(int x, int y) when x == y -> "diagonale " + x;
        case Point(int x, int y) ->             "Punto: " + x + "," + y;
        default ->                              "non è un punto"
    }
}