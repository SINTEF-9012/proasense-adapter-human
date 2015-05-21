/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package eu.proasense.internal;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum VariableType implements org.apache.thrift.TEnum {
  LONG(0),
  STRING(1),
  DOUBLE(2),
  BLOB(3);

  private final int value;

  private VariableType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static VariableType findByValue(int value) { 
    switch (value) {
      case 0:
        return LONG;
      case 1:
        return STRING;
      case 2:
        return DOUBLE;
      case 3:
        return BLOB;
      default:
        return null;
    }
  }
}