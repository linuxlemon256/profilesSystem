package com.example.profilessystem.server;
import com.example.profilessystem.mapper.ModerationMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import java.text.Normalizer;
import java.util.*;
@Service
public class ModerationService {
 private final ModerationMapper mapper;
 private volatile Snapshot snapshot;
 private record Snapshot(Node root, List<Map<String,Object>> rows, long expires) {}
 private static class Node { final Map<Integer,Node> children=new HashMap<>(); boolean terminal; }
 public ModerationService(ModerationMapper mapper){this.mapper=mapper;}
 public static String normalize(String input){
  if(input==null)return "";
  String value=input;
  for(int i=0;i<2;i++)value=HtmlUtils.htmlUnescape(value);
  value=Normalizer.normalize(value,Normalizer.Form.NFKC).toLowerCase(Locale.ROOT);
  StringBuilder out=new StringBuilder();
  value.codePoints().filter(Character::isLetterOrDigit).forEach(out::appendCodePoint);
  return out.toString();
 }
 private synchronized Snapshot load(){
  if(snapshot!=null && snapshot.expires()>System.nanoTime())return snapshot;
  List<Map<String,Object>> rows=mapper.all();
  if(rows.size()>20000)throw new IllegalStateException("词库超过容量限制");
  Node root=new Node();
  for(var row:rows){String word=normalize(String.valueOf(row.get("word")));if(word.isEmpty())continue;
   Node node=root;for(int cp:word.codePoints().toArray())node=node.children.computeIfAbsent(cp,k->new Node());node.terminal=true;
  }
  return snapshot=new Snapshot(root,List.copyOf(rows),System.nanoTime()+5_000_000_000L);
 }
 public List<Map<String,Object>> words(){return load().rows();}
 public void check(String text){
  if(text==null||text.isEmpty())return;
  if(text.length()>20000)throw new IllegalArgumentException("文本过长，请缩短后提交");
  Snapshot current;
  try{current=load();}catch(Exception e){throw new ModerationUnavailable();}
  if(current.rows().isEmpty())throw new ModerationUnavailable();
  int[] chars=normalize(text).codePoints().toArray();
  for(int i=0;i<chars.length;i++){Node node=current.root();
   for(int j=i;j<chars.length && j<i+64;j++){node=node.children.get(chars[j]);if(node==null)break;
    if(node.terminal)throw new IllegalArgumentException("内容包含不适宜词语，请修改后提交");
   }
  }
 }
 public synchronized int add(String input){
  if(input==null||input.length()>20000)throw new IllegalArgumentException("请填写词语，每批不超过200行");
  String[] lines=input.split("\\R");
  if(lines.length>200)throw new IllegalArgumentException("每批最多200行");
  Map<String,String> words=new LinkedHashMap<>();
  for(String line:lines){String word=line.strip();if(word.isEmpty())continue;String normalized=normalize(word);
   int length=normalized.codePointCount(0,normalized.length());
   if(word.length()>128||length<2||length>64)throw new IllegalArgumentException("词语需包含2至64个有效文字或数字，原文不超过128字符");
   words.putIfAbsent(normalized,word);
  }
  if(words.isEmpty())throw new IllegalArgumentException("请填写词语");
  Set<String> existing=new HashSet<>();for(var row:load().rows())existing.add(normalize(String.valueOf(row.get("word"))));
  if(existing.size()+words.keySet().stream().filter(x->!existing.contains(x)).count()>20000)throw new IllegalArgumentException("词库最多20000条");
  int added=0;try{for(var word:words.entrySet())added+=mapper.add(word.getValue(),word.getKey());}finally{snapshot=null;}
  return added;
 }
 public synchronized int delete(long id){try{return mapper.delete(id);}finally{snapshot=null;}}
 public static class ModerationUnavailable extends RuntimeException {}
}
