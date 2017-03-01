package org.jetbrains.java.decompiler.main.collectors;

import org.jetbrains.java.decompiler.main.DecompilerContext;
import org.jetbrains.java.decompiler.main.TextBuffer;

import java.util.HashMap;
import java.util.Map.Entry;

public class BytecodeSourceMapper {

  private int offset_total;

  // class, method, bytecode offset, source line
  private HashMap<String, HashMap<String, HashMap<Integer, Integer>>> mapping = new HashMap<String, HashMap<String, HashMap<Integer, Integer>>>();

  public void addMapping(String classname, String methodname, int bytecode_offset, int source_line) {

    HashMap<String, HashMap<Integer, Integer>> class_mapping = mapping.get(classname);
    if(class_mapping == null) {
      mapping.put(classname, class_mapping = new HashMap<String, HashMap<Integer, Integer>>());
    }

    HashMap<Integer, Integer> method_mapping = class_mapping.get(methodname);
    if(method_mapping == null) {
      class_mapping.put(methodname, method_mapping = new HashMap<Integer, Integer>());
    }

    // don't overwrite
    if(!method_mapping.containsKey(bytecode_offset)) {
      method_mapping.put(bytecode_offset, source_line);
    }
  }

  public void addTracer(String classname, String methodname, BytecodeMappingTracer tracer) {
    for(Entry<Integer, Integer> entry : tracer.getMapping().entrySet()) {
      addMapping(classname, methodname, entry.getKey(), entry.getValue());
    }
  }

  public void dumpMapping(TextBuffer buffer) {

    String lineSeparator = DecompilerContext.getNewLineSeparator();

    for(Entry<String, HashMap<String, HashMap<Integer, Integer>>> class_entry : mapping.entrySet()) {
      HashMap<String, HashMap<Integer, Integer>> class_mapping = class_entry.getValue();
      buffer.append("class " + class_entry.getKey() + "{" + lineSeparator);

      boolean is_first_method = true;

      for(Entry<String, HashMap<Integer, Integer>> method_entry : class_mapping.entrySet()) {
        HashMap<Integer, Integer> method_mapping = method_entry.getValue();

        if(!is_first_method) {
          buffer.appendLineSeparator();
        }
        buffer.appendIndent(1).append("method " + method_entry.getKey() + "{" + lineSeparator);

        for(Entry<Integer, Integer> line : method_mapping.entrySet()) {
          buffer.appendIndent(2).append(line.getKey().toString()).appendIndent(2).append((line.getValue() + offset_total) + lineSeparator);
        }
        buffer.appendIndent(1).append("}").appendLineSeparator();
        is_first_method = false;
      }
      buffer.append("}").appendLineSeparator();
    }
  }

  public int getTotalOffset() {
    return offset_total;
  }

  public void setTotalOffset(int offset_total) {
    this.offset_total = offset_total;
  }

  public void addTotalOffset(int offset_total) {
    this.offset_total += offset_total;
  }


}
