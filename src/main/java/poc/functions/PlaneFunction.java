package poc.functions;

import java.util.function.Function;

public abstract class PlaneFunction implements Function<Integer[][], Integer> {
  public abstract int tableSize();
}
