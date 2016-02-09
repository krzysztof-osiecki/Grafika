package poc.data;

public class Tuple3Ints {
  public int _1;
  public int _2;
  public int _3;

  private Tuple3Ints(int _1, int _2, int _3) {
    this._1 = _1;
    this._2 = _2;
    this._3 = _3;
  }

  public static Tuple3Ints of(int _1, int _2, int _3){
        return new Tuple3Ints(_1, _2, _3);
  }
}
