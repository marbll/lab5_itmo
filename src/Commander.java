package Labs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class Commander {
   private CollectionManager manager;
   private String userCommand;
   private String[] finalUserCommand;
   private Scanner commandReader;
   private ArrayList<String> commands_of_script;
   protected static ArrayList<String> last_commands = new ArrayList();

   public Commander(CollectionManager manager) {
      this.commandReader = new Scanner(System.in);
      this.commands_of_script = new ArrayList();
      this.userCommand = "";

      for(int i = 0; i < 8; ++i) {
         last_commands.add(".");
      }

      this.manager = manager;
   }

   public void interactiveMod() {
      while(true) {
         try {
            if (!this.userCommand.equals("exit")) {
               System.out.print(">> ");
               this.userCommand = this.commandReader.nextLine();
               this.finalUserCommand = this.userCommand.trim().split(" ", 2);
               this.choseCommand();
               continue;
            }
         } catch (NoSuchElementException var2) {
            System.exit(1);
         }

         return;
      }
   }

   public void choseCommand() {
      try {
         last_commands.add(this.finalUserCommand[0]);
         String var1 = this.finalUserCommand[0];
         byte var2 = -1;
         switch(var1.hashCode()) {
         case -1089336728:
            if (var1.equals("average_of_average_point")) {
               var2 = 14;
            }
            break;
         case -1020102443:
            if (var1.equals("execute_script")) {
               var2 = 6;
            }
            break;
         case -838846263:
            if (var1.equals("update")) {
               var2 = 7;
            }
            break;
         case -526296440:
            if (var1.equals("remove_by_id")) {
               var2 = 8;
            }
            break;
         case -523060427:
            if (var1.equals("remove_first")) {
               var2 = 13;
            }
            break;
         case -270246050:
            if (var1.equals("print_field_descending_average_point")) {
               var2 = 15;
            }
            break;
         case 0:
            if (var1.equals("")) {
               var2 = 0;
            }
            break;
         case 96417:
            if (var1.equals("add")) {
               var2 = 5;
            }
            break;
         case 3127582:
            if (var1.equals("exit")) {
               var2 = 1;
            }
            break;
         case 3198785:
            if (var1.equals("help")) {
               var2 = 2;
            }
            break;
         case 3237038:
            if (var1.equals("info")) {
               var2 = 3;
            }
            break;
         case 3522941:
            if (var1.equals("save")) {
               var2 = 10;
            }
            break;
         case 3529469:
            if (var1.equals("show")) {
               var2 = 4;
            }
            break;
         case 94746189:
            if (var1.equals("clear")) {
               var2 = 9;
            }
            break;
         case 903462125:
            if (var1.equals("filter_by_average_point")) {
               var2 = 16;
            }
            break;
         case 926934164:
            if (var1.equals("history")) {
               var2 = 12;
            }
            break;
         case 982730559:
            if (var1.equals("remove_greater")) {
               var2 = 11;
            }
         }

         switch(var2) {
         case 0:
         case 1:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               System.exit(1);
            }
            break;
         case 2:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               this.manager.help();
            } else {
               this.manager.help();
            }
            break;
         case 3:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               System.out.println(this.manager.toString());
            } else {
               System.out.println(this.manager.toString());
            }
            break;
         case 4:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               this.manager.show();
            } else {
               this.manager.show();
            }
            break;
         case 5:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               this.manager.add();
            } else {
               this.manager.add();
            }
            break;
         case 6:
            ArrayList<String> commands_of_script = new ArrayList();
            this.manager.execute_script(this.finalUserCommand[1], commands_of_script);
            if (commands_of_script.size() == 0) {
               break;
            }

            Iterator var8 = commands_of_script.iterator();

            while(var8.hasNext()) {
               String command = (String)var8.next();
               System.out.println(">> " + command);
               this.finalUserCommand = command.trim().split(" ", 2);
               this.choseCommand();
            }

            CollectionManager.scripts.remove(CollectionManager.scripts.size() - 1);
            break;
         case 7:
            this.manager.update(this.finalUserCommand[1]);
            break;
         case 8:
            this.manager.remove_by_id(this.finalUserCommand[1]);
            break;
         case 9:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               this.manager.clear();
            } else {
               this.manager.clear();
            }
            break;
         case 10:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               this.manager.save();
            } else {
               this.manager.save();
            }
            break;
         case 11:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               this.manager.remove_greater();
            } else {
               this.manager.remove_greater();
            }
            break;
         case 12:
            int i;
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               last_commands.remove(last_commands.size() - 1);

               for(i = 0; i < 8; ++i) {
                  if (!((String)last_commands.get(i)).equals(".")) {
                     System.out.println((String)last_commands.get(i));
                  }
               }

               last_commands.add("history");
               break;
            }

            last_commands.remove(last_commands.size() - 1);

            for(i = 0; i < 8; ++i) {
               if (!((String)last_commands.get(i)).equals(".")) {
                  System.out.println((String)last_commands.get(i));
               }
            }

            last_commands.add("history");
            break;
         case 13:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               this.manager.remove_first();
            } else {
               this.manager.remove_first();
            }
            break;
         case 14:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               this.manager.average_of_average_point();
            } else {
               this.manager.average_of_average_point();
            }
            break;
         case 15:
            if (this.finalUserCommand.length > 1) {
               if (!this.finalUserCommand[1].equals("")) {
                  throw new InputException();
               }

               this.manager.print_field_descending_average_point();
            } else {
               this.manager.print_field_descending_average_point();
            }
            break;
         case 16:
            this.manager.filter_by_average_point(this.finalUserCommand[1]);
            break;
         default:
            throw new InputException();
         }

         last_commands.remove(0);
      } catch (InputException var6) {
         System.out.println("Неопознанная команда. Наберите 'help' для справки.");
         last_commands.remove(last_commands.size() - 1);
      } catch (ArrayIndexOutOfBoundsException var7) {
         System.out.println("Отсутствует аргумент");
         last_commands.remove(last_commands.size() - 1);
      }

   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Commander)) {
         return false;
      } else {
         Commander commander = (Commander)o;
         return this.manager.equals(commander.manager);
      }
   }

   public int hashCode() {
      int result = Objects.hash(new Object[]{this.manager, this.userCommand});
      result = 31 * result + Arrays.hashCode(this.finalUserCommand);
      return result;
   }
}
