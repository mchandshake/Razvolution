/* Decompiler 61ms, total 440ms, lines 230 */
package wtf.evolution.model.models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class OBJLoader {
   private static final String COMMENT = "#";
   private static final String FACE = "f";
   private static final String POSITION = "v";
   private static final String TEX_COORDS = "vt";
   private static final String NORMAL = "vn";
   private static final String NEW_OBJECT = "o";
   private static final String NEW_GROUP = "g";
   private static final String USE_MATERIAL = "usemtl";
   private static final String NEW_MATERIAL = "mtllib";
   private boolean hasNormals = false;
   private boolean hasTexCoords = false;

   public HashMap<ObjObject, IndexedModel> loadModel(String startPath, String res) throws Exception {
      try {
         this.hasNormals = true;
         this.hasTexCoords = true;
         IndexedModel result = new IndexedModel();
         IndexedModel normalModel = new IndexedModel();
         String[] lines = res.split("\n|\r");
         int posOffset = 0;
         boolean indicesOffset = false;
         int texOffset = 0;
         int normOffset = 0;
         ArrayList<Vector3f> positions = new ArrayList();
         ArrayList<Vector2f> texCoords = new ArrayList();
         ArrayList<Vector3f> normals = new ArrayList();
         ArrayList<wtf.evolution.model.models.OBJLoader.OBJIndex> indices = new ArrayList();
         ArrayList<Material> materials = new ArrayList();
         HashMapWithDefault<wtf.evolution.model.models.OBJLoader.OBJIndex, Integer> resultIndexMap = new HashMapWithDefault();
         HashMapWithDefault<Integer, Integer> normalIndexMap = new HashMapWithDefault();
         HashMapWithDefault<Integer, Integer> indexMap = new HashMapWithDefault();
         resultIndexMap.setDefault(-1);
         normalIndexMap.setDefault(-1);
         indexMap.setDefault(-1);
         HashMap<ObjObject, IndexedModel> map = new HashMap();
         ObjObject currentObject = null;
         HashMap<ObjObject, IndexedModel[]> objects = new HashMap();
         objects.put(currentObject = new ObjObject("main"), new IndexedModel[]{result, normalModel});
         String[] var21 = lines;
         int var22 = lines.length;

         int i;
         for(i = 0; i < var22; ++i) {
            String line = var21[i];
            if (line != null && !line.trim().equals("")) {
               String[] parts = trim(line.split(" "));
               if (parts.length != 0 && !parts[0].equals("#")) {
                  if (parts[0].equals("v")) {
                     positions.add(new Vector3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])));
                  } else if (parts[0].equals("f")) {
                     for(int i1 = 0; i1 < parts.length - 3; ++i1) {
                        indices.add(this.parseOBJIndex(parts[1], posOffset, texOffset, normOffset));
                        indices.add(this.parseOBJIndex(parts[2 + i1], posOffset, texOffset, normOffset));
                        indices.add(this.parseOBJIndex(parts[3 + i1], posOffset, texOffset, normOffset));
                     }
                  } else if (parts[0].equals("vn")) {
                     normals.add(new Vector3f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])));
                  } else if (parts[0].equals("vt")) {
                     texCoords.add(new Vector2f(Float.parseFloat(parts[1]), Float.parseFloat(parts[2])));
                  } else if (parts[0].equals("mtllib")) {
                     String path = startPath + parts[1];
                     MtlMaterialLib material = new MtlMaterialLib(path);
                     material.parse(this.read(OBJLoader.class.getResourceAsStream(path)));
                     materials.addAll(material.getMaterials());
                  } else if (parts[0].equals("usemtl")) {
                     currentObject.material = this.getMaterial(materials, parts[1]);
                  } else if (parts[0].equals("o") || parts[0].equals("g")) {
                     result.getObjIndices().addAll(indices);
                     normalModel.getObjIndices().addAll(indices);
                     result = new IndexedModel();
                     normalModel = new IndexedModel();
                     indices.clear();
                     objects.put(currentObject = new ObjObject(parts[1]), new IndexedModel[]{result, normalModel});
                  }
               }
            }
         }

         result.getObjIndices().addAll(indices);
         normalModel.getObjIndices().addAll(indices);
         Iterator it = objects.keySet().iterator();

         while(true) {
            do {
               if (!it.hasNext()) {
                  return map;
               }

               ObjObject object = (ObjObject)it.next();
               result = ((IndexedModel[])((IndexedModel[])objects.get(object)))[0];
               normalModel = ((IndexedModel[])((IndexedModel[])objects.get(object)))[1];
               indices = result.getObjIndices();
               map.put(object, result);
               object.center = result.computeCenter();

               for(i = 0; i < indices.size(); ++i) {
                  wtf.evolution.model.models.OBJLoader.OBJIndex current = (wtf.evolution.model.models.OBJLoader.OBJIndex)indices.get(i);
                  Vector3f pos = (Vector3f)positions.get(current.positionIndex);
                  Vector2f texCoord;
                  if (this.hasTexCoords) {
                     texCoord = (Vector2f)texCoords.get(current.texCoordsIndex);
                  } else {
                     texCoord = new Vector2f();
                  }

                  Vector3f normal;
                  if (this.hasNormals) {
                     try {
                        normal = (Vector3f)normals.get(current.normalIndex);
                     } catch (Exception var32) {
                        normal = new Vector3f();
                     }
                  } else {
                     normal = new Vector3f();
                  }

                  int modelVertexIndex = (Integer)resultIndexMap.get(current);
                  if (modelVertexIndex == -1) {
                     resultIndexMap.put(current, result.getPositions().size());
                     modelVertexIndex = result.getPositions().size();
                     result.getPositions().add(pos);
                     result.getTexCoords().add(texCoord);
                     if (this.hasNormals) {
                        result.getNormals().add(normal);
                     }

                     result.getTangents().add(new Vector3f());
                  }

                  int normalModelIndex = (Integer)normalIndexMap.get(current.positionIndex);
                  if (normalModelIndex == -1) {
                     normalModelIndex = normalModel.getPositions().size();
                     normalIndexMap.put(current.positionIndex, normalModelIndex);
                     normalModel.getPositions().add(pos);
                     normalModel.getTexCoords().add(texCoord);
                     normalModel.getNormals().add(normal);
                     normalModel.getTangents().add(new Vector3f());
                  }

                  result.getIndices().add(modelVertexIndex);
                  normalModel.getIndices().add(normalModelIndex);
                  indexMap.put(modelVertexIndex, normalModelIndex);
               }
            } while(this.hasNormals);

            normalModel.computeNormals();

            for(i = 0; i < result.getNormals().size(); ++i) {
               result.getNormals().add(normalModel.getNormals().get((Integer)indexMap.get(i)));
            }
         }
      } catch (Exception var33) {
         throw new RuntimeException("Error while loading model", var33);
      }
   }

   private Material getMaterial(ArrayList<Material> materials, String id) {
      Iterator var3 = materials.iterator();

      while(var3.hasNext()) {
         Material mat = (Material)var3.next();
         if (mat.getName().equals(id)) {
            return mat;
         }
      }

      return null;
   }

   protected String read(InputStream resource) throws IOException {
      byte[] buffer = new byte[65565];
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      int i;
      while((i = resource.read(buffer, 0, buffer.length)) != -1) {
         out.write(buffer, 0, i);
      }

      out.flush();
      out.close();
      return new String(out.toByteArray(), "UTF-8");
   }

   public wtf.evolution.model.models.OBJLoader.OBJIndex parseOBJIndex(String token, int posOffset, int texCoordsOffset, int normalOffset) {
      wtf.evolution.model.models.OBJLoader.OBJIndex index = new wtf.evolution.model.models.OBJLoader.OBJIndex();
      String[] values = token.split("/");
      index.positionIndex = Integer.parseInt(values[0]) - 1 - posOffset;
      if (values.length > 1) {
         if (values[1] != null && !values[1].equals("")) {
            index.texCoordsIndex = Integer.parseInt(values[1]) - 1 - texCoordsOffset;
         }

         this.hasTexCoords = true;
         if (values.length > 2) {
            index.normalIndex = Integer.parseInt(values[2]) - 1 - normalOffset;
            this.hasNormals = true;
         }
      }

      return index;
   }

   public static String[] trim(String[] split) {
      ArrayList<String> strings = new ArrayList();
      String[] var2 = split;
      int var3 = split.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String s = var2[var4];
         if (s != null && !s.trim().equals("")) {
            strings.add(s);
         }
      }

      return (String[])((String[])strings.toArray(new String[0]));
   }
}
