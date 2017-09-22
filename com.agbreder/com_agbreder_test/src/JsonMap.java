import java.util.HashMap;
import java.util.Set;

/**
 * Mapa de JSON
 * 
 * @author bernardobreder
 * 
 */
public class JsonMap extends HashMap<Object, Object> {

  /**
   * {@inheritDoc}
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    Set<Object> keySet = this.keySet();
    for (Object key : keySet) {
      sb.append("\"");
      sb.append(key);
      sb.append("\"");
      sb.append(":");
      sb.append(this.get(key));
      sb.append(",");
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append("}");
    return sb.toString();
  }

}
