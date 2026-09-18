# Judey User Guide

Judey is a playful astronaut-cat assistant for keeping your tasks in orbit.
The interface uses a dark space theme with neon Japan-night-city accents.

## Getting help

Enter `help` to display all available commands and syntax examples. The command does not accept arguments.

## Commands

### Creating tasks

- `todo <description>` — create a todo task.
- `deadline <description> /by <date and time>` — create a deadline task.
- `event <description> /from <date and time> /to <date and time>` — create an event task.

### Managing tasks

- `list` — display all tasks.
- `mark <task number>` — mark a task as done.
- `unmark <task number>` — mark a task as not done.
- `delete <task number>` — delete a task.

### Searching and filtering

- `events-on <date>` — display events on a date.

### Application

- `help` — display this guide.
- `bye` — exit Judey.

## Error guidance

In the GUI, invalid commands remain visible in the original user-message bubble. Judey then shows a red error bubble
with a correction template. The first missing or invalid placeholder is highlighted. For example, entering `event`
shows:

```text
event <description> /from <start date and start time: d/M/yyyy HHmm> /to <end date and end time: d/M/yyyy HHmm>
```

The command syntax and stored task format are unchanged. Invalid commands are not saved. The command-line interface
continues to display plain-text errors without GUI highlighting.
