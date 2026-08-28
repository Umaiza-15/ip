# Console UI test plan

## Test environment

- Compile command: `javac -d out src/main/java/*.java`
- Run command: `java -cp out Judey`
- The `test-ui` skill compiles with Java 25 before each session.

## Test case: Exit politely

### Aim

Verify that the application accepts the `bye` command and exits with its farewell message.

### Inputs

```text
bye
```

### Expected output

```text
----------------------------------------
JJJJJJ  uu   uu  dddddd   eeeeeee  yy   yy
   JJ   uu   uu  dd   dd  ee       yy   yy
   JJ   uu   uu  dd   dd  eeeee     yyyyy
JJ JJ   uu   uu  dd   dd  ee         yyy
 JJJ     uuuu u  dddddd   eeeeeee    yyy

Hello! I'm Judey.
What can I do for you?
----------------------------------------
----------------------------------------
Bye. Hope to see you again soon!
----------------------------------------
```

## Test case: Add and list a todo

### Aim

Verify that a `todo` command adds a task and that `list` displays the added task before the session ends.

### Inputs

```text
todo read book
list
bye
```

### Expected output

```text
----------------------------------------
JJJJJJ  uu   uu  dddddd   eeeeeee  yy   yy
   JJ   uu   uu  dd   dd  ee       yy   yy
   JJ   uu   uu  dd   dd  eeeee     yyyyy
JJ JJ   uu   uu  dd   dd  ee         yyy
 JJJ     uuuu u  dddddd   eeeeeee    yyy

Hello! I'm Judey.
What can I do for you?
----------------------------------------
----------------------------------------
Got it. I've added this task 
  [T][ ] read book

Now you have 1 tasks in this list.

----------------------------------------
----------------------------------------
Here are the tasks in your list: 
1.[T][ ] read book

----------------------------------------
----------------------------------------
Bye. Hope to see you again soon!
----------------------------------------
```

## Test case: Reject missing and unknown commands

### Aim

Verify that invalid input is reported through Judey's error handling and that the chatbot continues accepting later commands.

### Inputs

```text
todo
blah
bye
```

### Expected output

```text
----------------------------------------
JJJJJJ  uu   uu  dddddd   eeeeeee  yy   yy
   JJ   uu   uu  dd   dd  ee       yy   yy
   JJ   uu   uu  dd   dd  eeeee     yyyyy
JJ JJ   uu   uu  dd   dd  ee         yyy
 JJJ     uuuu u  dddddd   eeeeeee    yyy

Hello! I'm Judey.
What can I do for you?
----------------------------------------
----------------------------------------
Oopsie! Your todo is missing its mission! Try: todo read book
----------------------------------------
----------------------------------------
Oopsie! Hmm, that command is still a mystery to me. Try todo, deadline, event, list, mark, unmark, delete, or bye.
----------------------------------------
----------------------------------------
Bye. Hope to see you again soon!
----------------------------------------
```

## Test case: Keep task state after invalid status commands

### Aim

Verify that invalid task numbers do not create or alter tasks, while valid status changes still update the intended task.

### Inputs

```text
mark 1
todo study
mark nope
mark 1
unmark 2
list
bye
```

### Expected output

```text
----------------------------------------
JJJJJJ  uu   uu  dddddd   eeeeeee  yy   yy
   JJ   uu   uu  dd   dd  ee       yy   yy
   JJ   uu   uu  dd   dd  eeeee     yyyyy
JJ JJ   uu   uu  dd   dd  ee         yyy
 JJJ     uuuu u  dddddd   eeeeeee    yyy

Hello! I'm Judey.
What can I do for you?
----------------------------------------
----------------------------------------
Oopsie! I checked twice, but that task number is not on the list.
----------------------------------------
----------------------------------------
Got it. I've added this task 
  [T][ ] study

Now you have 1 tasks in this list.

----------------------------------------
----------------------------------------
Oopsie! Task numbers are whole numbers only; no decimals or letters this time!
----------------------------------------
----------------------------------------
Nice! I've marked this task as done: 
  [T][X] study

----------------------------------------
----------------------------------------
Oopsie! I checked twice, but that task number is not on the list.
----------------------------------------
----------------------------------------
Here are the tasks in your list: 
1.[T][X] study

----------------------------------------
----------------------------------------
Bye. Hope to see you again soon!
----------------------------------------
```

## Test case: Delete a task

### Aim

Verify that `delete` removes the requested task and renumbers the remaining tasks.

### Inputs

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
delete 3
list
bye
```

### Expected output

```text
----------------------------------------
JJJJJJ  uu   uu  dddddd   eeeeeee  yy   yy
   JJ   uu   uu  dd   dd  ee       yy   yy
   JJ   uu   uu  dd   dd  eeeee     yyyyy
JJ JJ   uu   uu  dd   dd  ee         yyy
 JJJ     uuuu u  dddddd   eeeeeee    yyy

Hello! I'm Judey.
What can I do for you?
----------------------------------------
----------------------------------------
Got it. I've added this task 
  [T][ ] read book

Now you have 1 tasks in this list.

----------------------------------------
----------------------------------------
Got it. I've added this task 
  [D][ ] return book (by: June 6th)

Now you have 2 tasks in this list.

----------------------------------------
----------------------------------------
Got it. I've added this task 
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)

Now you have 3 tasks in this list.

----------------------------------------
----------------------------------------
Noted. I've removed this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 2 tasks in the list.
----------------------------------------
----------------------------------------
Here are the tasks in your list: 
1.[T][ ] read book
2.[D][ ] return book (by: June 6th)

----------------------------------------
----------------------------------------
Bye. Hope to see you again soon!
----------------------------------------
```

## Test case: Keep task state after malformed deadline and event commands

### Aim

Verify that malformed deadline and event commands add nothing, while the later valid commands create exactly the requested tasks.

### Inputs

```text
deadline report
deadline report /by Friday
event meeting /from 2pm
event meeting /from 2pm /to 3pm
list
bye
```

### Expected output

```text
----------------------------------------
JJJJJJ  uu   uu  dddddd   eeeeeee  yy   yy
   JJ   uu   uu  dd   dd  ee       yy   yy
   JJ   uu   uu  dd   dd  eeeee     yyyyy
JJ JJ   uu   uu  dd   dd  ee         yyy
 JJJ     uuuu u  dddddd   eeeeeee    yyy

Hello! I'm Judey.
What can I do for you?
----------------------------------------
----------------------------------------
Oopsie! That deadline needs a little more sparkle: deadline report /by Friday
----------------------------------------
----------------------------------------
Got it. I've added this task 
  [D][ ] report (by: Friday)

Now you have 1 tasks in this list.

----------------------------------------
----------------------------------------
Oopsie! That event needs a name, /from time, and /to time to get on my calendar.
----------------------------------------
----------------------------------------
Got it. I've added this task 
  [E][ ] meeting (from: 2pm to: 3pm)

Now you have 2 tasks in this list.

----------------------------------------
----------------------------------------
Here are the tasks in your list: 
1.[D][ ] report (by: Friday)
2.[E][ ] meeting (from: 2pm to: 3pm)

----------------------------------------
----------------------------------------
Bye. Hope to see you again soon!
----------------------------------------
```
