package Labs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;
import javax.management.modelmbean.XMLParseException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class CollectionManager {
   private PriorityQueue<LabWork> labWorks = new PriorityQueue();
   protected static ArrayList<String> scripts = new ArrayList();
   private String collectionPath;
   private File xmlCollection;
   private Date initDate;
   private Iterator iterator;
   private boolean wasStart;
   private Integer globalId;
   private LocalDate globalCreationDate;
   protected static HashMap<String, String> manual;

   public CollectionManager(String collectionPath) {
      this.iterator = this.labWorks.iterator();
      this.globalId = null;
      this.globalCreationDate = null;
      manual = new HashMap();
      manual.put("help", "вывести справку по доступным командам");
      manual.put("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
      manual.put("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
      manual.put("add {element}", "добавить новый элемент в коллекцию");
      manual.put("update id {element}", "обновить значение элемента коллекции, id которого равен заданному");
      manual.put("remove_by_id id", "удалить элемент из коллекции по его id");
      manual.put("clear", "очистить коллекцию");
      manual.put("save", "сохранить коллекцию в файл");
      manual.put("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме");
      manual.put("exit", "завершить программу (без сохранения в файл)");
      manual.put("remove_first", " удалить первый элемент из коллекции");
      manual.put("remove_greater {element}", " удалить из коллекции все элементы, превышающие заданный");
      manual.put("history", "вывести последние 8 команд (без их аргументов)");
      manual.put("average_of_average_point", " вывести среднее значение поля averagePoint для всех элементов коллекции");
      manual.put("filter_by_average_point averagePoint", " вывести элементы, значение поля averagePoint которых равно заданному");
      manual.put("print_field_descending_average_point", " вывести значения поля averagePoint всех элементов в порядке убывания");

      try {
         File file = new File(collectionPath);
         if (!file.exists()) {
            throw new FileNotFoundException();
         }

         this.xmlCollection = file;
         this.collectionPath = collectionPath;
      } catch (FileNotFoundException var3) {
         System.out.println("Путь до файла xml нужно передать через переменную окружения. Файл по указанному пути не существует.");
         System.exit(1);
      }

      this.load();
      this.initDate = new Date();
      this.wasStart = true;
   }

   public void help() {
      System.out.println("Доступные к использованию команды:");
      manual.keySet().forEach((p) -> {
         System.out.println(p + " - " + (String)manual.get(p));
      });
   }

   public void show() {
      if (this.labWorks.size() != 0) {
         PrintStream var10001 = System.out;
         this.labWorks.forEach(var10001::println);
      } else {
         System.out.println("В коллекции отсутствуют элементы. Выполнение команды невозможно.");
      }

   }

   public LabWork newLabWork() {
      Scanner reader = new Scanner(System.in);
      System.out.print("Введите name: ");

      String name;
      for(name = reader.nextLine(); name.equals(""); name = reader.nextLine()) {
         System.out.println("Поле не может быть null или пустой строкой ");
         System.out.print("Введите name: ");
      }

      System.out.println("Введите coordinates: ");
      boolean p = false;
      int x = 0;

      String a;
      while(!p) {
         System.out.print("Введите x: ");
         a = reader.nextLine();

         try {
            x = Integer.parseInt(a);
            if (x > -985) {
               p = true;
            } else {
               System.out.println("Значение поля должно быть больше -985");
            }
         } catch (NumberFormatException var22) {
            System.out.println("Аргумент не является значением типа int");
         }
      }

      Double y = null;

      while(p) {
         System.out.print("Введите y: ");
         a = reader.nextLine();

         try {
            y = Double.parseDouble(a);
            p = false;
         } catch (NumberFormatException var21) {
            System.out.println("Аргумент не является значением типа Double");
         }
      }

      long minimalPoint = 0L;

      while(!p) {
         System.out.print("Введите minimalPoint: ");
         a = reader.nextLine();

         try {
            minimalPoint = Long.parseLong(a);
            if (minimalPoint > 0L) {
               p = true;
            } else {
               System.out.println("Значение поля должно быть больше 0");
            }
         } catch (NumberFormatException var20) {
            System.out.println("Аргумент не является значением типа long");
         }
      }

      System.out.print("Введите description: ");

      String description;
      for(description = reader.nextLine(); description.equals(""); description = reader.nextLine()) {
         System.out.println("Поле не может быть null или пустой строкой ");
         System.out.print("Введите description: ");
      }

      Double averagePoint = null;

      while(p) {
         System.out.print("Введите averagePoint: ");
         a = reader.nextLine();

         try {
            averagePoint = Double.parseDouble(a);
            if (averagePoint > 0.0D) {
               p = false;
            } else {
               System.out.println("Значение поля должно быть больше 0");
            }
         } catch (NumberFormatException var19) {
            System.out.println("Аргумент не является значением типа Double");
         }
      }

      System.out.print("Введите Difficulty (VERY_EASY, EASY, NORMAL, INSANE, null): ");

      String difficulty_s;
      for(difficulty_s = reader.nextLine(); !difficulty_s.equals("") && !difficulty_s.equals("VERY_EASY") && !difficulty_s.equals("EASY") && !difficulty_s.equals("NORMAL") && !difficulty_s.equals("INSANE"); difficulty_s = reader.nextLine()) {
         System.out.println("Значение поля неверное");
         System.out.print("Введите Difficulty (VERY_EASY, EASY, NORMAL, INSANE, null): ");
      }

      Difficulty difficulty = null;
      if (!difficulty_s.equals("")) {
         difficulty = Difficulty.valueOf(difficulty_s);
      }

      System.out.println("Введите Discipline: ");
      System.out.print("Введите name: ");

      String nameDiscipline;
      for(nameDiscipline = reader.nextLine(); nameDiscipline.equals(""); nameDiscipline = reader.nextLine()) {
         System.out.println("Поле не может быть null");
         System.out.print("Введите name: ");
      }

      Long labsCount = null;

      while(!p) {
         System.out.print("Введите labsCount: ");
         a = reader.nextLine();

         try {
            if (a.equals("")) {
               p = true;
            } else {
               labsCount = Long.parseLong(a);
               p = true;
            }
         } catch (NumberFormatException var18) {
            System.out.println("Аргумент не является значением типа Long");
         }
      }

      Integer id = null;
      if (this.globalId != null) {
         id = this.globalId;
      } else {
         label109:
         while(true) {
            while(true) {
               if (!p) {
                  break label109;
               }

               p = false;
               Random random = new Random();
               id = random.nextInt(10000) + 1;
               this.iterator = this.labWorks.iterator();

               while(this.iterator.hasNext()) {
                  LabWork l = (LabWork)this.iterator.next();
                  if (l.getId().equals(id)) {
                     p = true;
                     break;
                  }
               }
            }
         }
      }

      LocalDate creationDate;
      if (this.globalCreationDate == null) {
         creationDate = LocalDate.now();
      } else {
         creationDate = this.globalCreationDate;
      }

      System.out.println("Все значения элемента успешно получены");
      return new LabWork(id, name, new Coordinates(x, y), creationDate, minimalPoint, description, averagePoint, difficulty, new Discipline(nameDiscipline, labsCount));
   }

   public void add() {
      this.labWorks.add(this.newLabWork());
      System.out.println("Элемент успешно добавлен");
   }

   public void update(String n) {
      if (this.labWorks.size() != 0) {
         try {
            Integer id = Integer.valueOf(n);
            boolean b = false;
            this.iterator = this.labWorks.iterator();
            this.globalId = id;

            while(this.iterator.hasNext()) {
               LabWork l = (LabWork)this.iterator.next();
               if (l.getId().equals(id)) {
                  this.globalCreationDate = l.getCreationDate();
                  this.labWorks.remove(l);
                  this.labWorks.add(this.newLabWork());
                  System.out.println("Элемент коллекции успешно обновлен.");
                  b = true;
                  break;
               }
            }

            this.globalId = null;
            this.globalCreationDate = null;
            if (!b) {
               System.out.println("В коллекции не найдено элемента с указанным id.");
            }
         } catch (NumberFormatException var5) {
            System.out.println("Аргумент не является значением типа Integer");
         }
      } else {
         System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
      }

   }

   public void remove_by_id(String n) {
      if (this.labWorks.size() != 0) {
         try {
            boolean b = false;
            Integer id = Integer.parseInt(n);
            this.iterator = this.labWorks.iterator();

            while(this.iterator.hasNext()) {
               LabWork l = (LabWork)this.iterator.next();
               if (l.getId().equals(id)) {
                  this.labWorks.remove(l);
                  System.out.println("Элемент коллекции успешно удален.");
                  b = true;
                  break;
               }
            }

            if (!b) {
               System.out.println("В коллекции не найдено элемента с указанным ключом.");
            } else {
               System.out.println("Команда успешно выполнена.");
            }
         } catch (NumberFormatException var5) {
            System.out.println("Аргумент не является значением типа Integer");
         }
      } else {
         System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
      }

   }

   public void clear() {
      this.labWorks.clear();
      System.out.println("Коллекция очищена.");
   }

   public void save() {
      try {
         Document doc = new Document();
         doc.setRootElement(new Element("Labs"));
         this.iterator = this.labWorks.iterator();

         while(this.iterator.hasNext()) {
            LabWork labWork = (LabWork)this.iterator.next();
            Element element = new Element("LabWork");
            element.addContent((new Element("id")).setText(String.valueOf(labWork.getId())));
            element.addContent((new Element("name")).setText(labWork.getName()));
            Element element_c = new Element("Coordinates");
            element_c.addContent((new Element("x")).setText(String.valueOf(labWork.getCoordinates().getX())));
            element_c.addContent((new Element("y")).setText(String.valueOf(labWork.getCoordinates().getY())));
            element.addContent(element_c);
            element.addContent((new Element("creationDate")).setText(String.valueOf(labWork.getCreationDate())));
            element.addContent((new Element("minimalPoint")).setText(String.valueOf(labWork.getMinimalPoint())));
            element.addContent((new Element("description")).setText(labWork.getDescription()));
            element.addContent((new Element("averagePoint")).setText(String.valueOf(labWork.getAveragePoint())));
            element.addContent((new Element("difficulty")).setText(String.valueOf(labWork.getDifficulty())));
            Element element_d = new Element("Discipline");
            element_d.addContent((new Element("name")).setText(labWork.getDiscipline().getName()));
            element_d.addContent((new Element("labsCount")).setText(String.valueOf(labWork.getDiscipline().getLabsCount())));
            element.addContent(element_d);
            doc.getRootElement().addContent(element);
         }

         if (!this.xmlCollection.canWrite()) {
            throw new SecurityException();
         }

         XMLOutputter xmlWriter = new XMLOutputter(Format.getPrettyFormat());
         xmlWriter.output(doc, new FileOutputStream(this.xmlCollection));
         System.out.println("Коллекция успешно сохранена в файл.");
      } catch (IOException var6) {
         System.out.println("Возникла непредвиденная ошибка. Коллекция не может быть записана в файл");
      } catch (SecurityException var7) {
         System.out.println("Файл защищён от записи. Невозможно сохранить коллекцию.");
      }

   }

   public void execute_script(String file, ArrayList<String> commands_of_script) {
      if (scripts.contains(file)) {
         Commander.last_commands.remove(Commander.last_commands.size() - 1);
         System.out.println("Могло произойти зацикливание при исполнении скрипта: " + file + "\nКоманда не будет выполнена. Переход к следующей команде");
      } else {
         File file1 = new File(file);
         if (!file1.exists()) {
            System.out.println("Файла с таким названием не существует.");
         } else if (!file1.canRead()) {
            System.out.println("Файл защищён от чтения. Невозможно выполнить скрипт.");
         } else {
            scripts.add(file);

            try {
               InputStreamReader commandReader = new InputStreamReader(new FileInputStream(file1));
               Throwable var5 = null;

               try {
                  StringBuilder s = new StringBuilder();

                  while(commandReader.ready()) {
                     s.append((char)commandReader.read());
                  }

                  String[] s1 = s.toString().split("\n");
                  commands_of_script.addAll(Arrays.asList(s1));
               } catch (Throwable var16) {
                  var5 = var16;
                  throw var16;
               } finally {
                  if (commandReader != null) {
                     if (var5 != null) {
                        try {
                           commandReader.close();
                        } catch (Throwable var15) {
                           var5.addSuppressed(var15);
                        }
                     } else {
                        commandReader.close();
                     }
                  }

               }
            } catch (IOException var18) {
               System.out.println("Невозможно считать скрипт");
               scripts.remove(scripts.size() - 1);
            }
         }
      }

   }

   public void remove_greater() {
      if (this.labWorks.size() != 0) {
         LabWork l = this.newLabWork();
         this.iterator = this.labWorks.iterator();

         while(this.iterator.hasNext()) {
            LabWork labWork = (LabWork)this.iterator.next();
            if (labWork.compareTo(l) > 0) {
               this.iterator.remove();
               this.labWorks.remove(labWork);
            }
         }

         System.out.println("Команда успешно выполнена.");
      } else {
         System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
      }

   }

   public void remove_first() {
      if (this.labWorks.size() != 0) {
         this.iterator = this.labWorks.iterator();
         LabWork l = (LabWork)this.iterator.next();
         this.iterator.remove();
         this.labWorks.remove(l);
         System.out.println("Команда успешно выполнена.");
      } else {
         System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
      }

   }

   public void average_of_average_point() {
      if (this.labWorks.size() != 0) {
         float sum_average_point = 0.0F;

         for(this.iterator = this.labWorks.iterator(); this.iterator.hasNext(); sum_average_point = (float)((double)sum_average_point + ((LabWork)this.iterator.next()).getAveragePoint())) {
         }

         System.out.println("Cреднее значение поля averagePoint для всех элементов коллекции: " + sum_average_point / (float)this.labWorks.size());
      } else {
         System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
      }

   }

   public void print_field_descending_average_point() {
      if (this.labWorks.size() != 0) {
         TreeSet<Double> averagePoint = new TreeSet();
         this.iterator = this.labWorks.iterator();

         while(this.iterator.hasNext()) {
            averagePoint.add(((LabWork)this.iterator.next()).getAveragePoint());
         }

         int a = averagePoint.size();

         for(int i = 0; i < a; ++i) {
            System.out.println(averagePoint.pollLast());
         }
      } else {
         System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
      }

   }

   public void filter_by_average_point(String averagePoint_s) {
      if (this.labWorks.size() != 0) {
         try {
            boolean b = true;
            Double averagePoint = Double.parseDouble(averagePoint_s);
            this.iterator = this.labWorks.iterator();

            while(this.iterator.hasNext()) {
               LabWork lab = (LabWork)this.iterator.next();
               if (lab.getAveragePoint().equals(averagePoint)) {
                  System.out.println(lab.toString());
                  b = false;
               }
            }

            if (b) {
               System.out.println("Элементов с значением поля averagePoint = " + averagePoint_s + " не найдено");
            }
         } catch (NumberFormatException var5) {
            System.out.println("Аргумент не является значением типа Integer");
         }
      } else {
         System.out.println("В коллекции отсутствуют элементы. Выполнение команды не возможно.");
      }

   }

   public void load() {
      int beginSize = this.labWorks.size();

      try {
         if (!this.xmlCollection.exists()) {
            throw new FileNotFoundException();
         }
      } catch (FileNotFoundException var26) {
         System.out.println("Файла по указанному пути не существует.");
         if (this.wasStart) {
            return;
         }

         System.exit(1);
      }

      try {
         if (!this.xmlCollection.canRead() || !this.xmlCollection.canWrite()) {
            throw new SecurityException();
         }
      } catch (SecurityException var25) {
         System.out.println("Файл защищён от чтения и/или записи. Для работы программы нужны оба разрешения.");
         if (this.wasStart) {
            return;
         }

         System.exit(1);
      }

      try {
         if (this.xmlCollection.length() == 0L) {
            throw new XMLParseException("");
         }
      } catch (XMLParseException var24) {
         System.out.println("Файл пуст.");
         if (this.wasStart) {
            return;
         }

         System.exit(1);
      }

      try {
         System.out.println("Идёт загрузка коллекции " + this.xmlCollection.getAbsolutePath());
         Document jdomDocument = createJDOMusingSAXParser(this.collectionPath);
         Element root = jdomDocument.getRootElement();
         List<Element> labWorkListElements = root.getChildren("LabWork");

         Integer id;
         String name;
         int x;
         Double y;
         LocalDate creationDate;
         long minimalPoint;
         String description;
         Double averagePoint;
         Difficulty difficulty;
         String nameDiscipline;
         Long labsCount;
         for(Iterator var5 = labWorkListElements.iterator(); var5.hasNext(); this.labWorks.add(new LabWork(id, name, new Coordinates(x, y), creationDate, minimalPoint, description, averagePoint, difficulty, new Discipline(nameDiscipline, labsCount)))) {
            Element lab = (Element)var5.next();
            id = Integer.parseInt(lab.getChildText("id"));
            name = lab.getChildText("name");
            List<Element> lab_c = lab.getChildren("Coordinates");
            x = Integer.parseInt(((Element)lab_c.get(0)).getChildText("x"));
            y = Double.parseDouble(((Element)lab_c.get(0)).getChildText("y"));
            creationDate = LocalDate.parse(lab.getChildText("creationDate"));
            minimalPoint = Long.parseLong(lab.getChildText("minimalPoint"));
            description = lab.getChildText("description");
            averagePoint = Double.parseDouble(lab.getChildText("averagePoint"));
            difficulty = null;
            String difficulty_s = lab.getChildText("difficulty");
            if (!difficulty_s.equals("null")) {
               difficulty = Difficulty.valueOf(difficulty_s);
            }

            List<Element> lab_d = lab.getChildren("Discipline");
            nameDiscipline = ((Element)lab_d.get(0)).getChildText("name");
            labsCount = null;
            String labsCount_s = ((Element)lab_d.get(0)).getChildText("labsCount");
            if (!labsCount_s.equals("null")) {
               labsCount = Long.parseLong(labsCount_s);
            }
         }
      } catch (Exception var23) {
         System.out.println("Не удалось загрузить коллекцию. Всё очеь-очень плохо!");
         if (this.wasStart) {
            return;
         }

         System.exit(1);
      }

      System.out.println("Коллекция успешно загружена. Добавлено " + (this.labWorks.size() - beginSize) + " элементов.");
   }

   private static Document createJDOMusingSAXParser(String fileName) throws JDOMException, IOException {
      SAXBuilder saxBuilder = new SAXBuilder();
      return saxBuilder.build(new File(fileName));
   }

   public String toString() {
      return "Тип коллекции: " + this.labWorks.getClass() + "\nДата инициализации: " + this.initDate + "\nКоличество элементов: " + this.labWorks.size();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof CollectionManager)) {
         return false;
      } else {
         CollectionManager manager = (CollectionManager)o;
         return this.labWorks.equals(manager.labWorks) && this.xmlCollection.equals(manager.xmlCollection) && this.initDate.equals(manager.initDate);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.labWorks, this.initDate});
   }
}
