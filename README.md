# 🛠️ **TaskTrackCLI**

***TaskTrackCLI*** is a simple and powerful command-line interface for managing tasks and execution rules. Built in Java, it helps you organize and track task statuses, define logical constraints, and perform bulk operations with ease.

## ✨ Features

- Create, remove, and list tasks or rules
- Update task status: `Waiting`, `Started`, `In Progress`, `Done`

| task status | meaning |
|-------------|----|
| Waiting     | Task is queued but not yet started. |
| Started     | Task has just begun. |
| InProgress  | Task is actively being worked on. |
| Done        | Task is completed successfully. |

- Define rules:
    - Task to Task rules - Example: A task can only start before another is done
    - Task to Date rules - Example: A task must start before a specific date
- Built-in circular rules dependency detection and prevention mechanism
- Clean command for bulk deletions:
    - Delete all tasks with a specific status (e.g. all `Done` tasks)
    - Delete all rules associated with a specific task
    - Delete all rules that have already been satisfied
- List violated date rules that can no longer be satisfied (e.g. missed deadlines)


## 📌 Notes

***TaskTrackCLI*** is terminal-first and designed for fast, rule-based task management. Useful for developers, teams, or anyone managing dependent workflows from the command line.


## 📜 commands
### add a task or a rule: 
```
add task --task-name <here is the task detail between ""> --task-status <TASK STATUS>
```
--task-status is optional (default: waiting)

```
add rule <task 1 status> <task 1 id> <rule relation> <task 2 status> <task 2 id>
```
Or
```
add rule <task 1 status> <task 1 id> <rule relation> <date:dd-MM-yyyy>
```

rule relation can be either : `after` or `before`.

### list tasks or rules: 
```
list task -f <white space separated list of task status>
```
-f optional flag to list only task with specific status included in the given list

```
list rule --type <rule type> --task-hash <task id>
```
both flags are optional
--type can be either `taskTotask` or `taskTodate`
--task-hash to list only rules applied on a certain task

### clean tasks: (bulk deletion of tasks with their associated rules)
```
clean <tasks to clean filter> --safe-check
```
tasks to clean filter to specify which tasks to be deleted, filter can be only one of the following :
`ALL`, `WAITING`, `STARTED`, `INPROGRESS`, `DONE`

--safe-check is an optional flag used to just list the tasks that are going to be deleted and their associated rules.

### remove a task or a rule:
```
remove <task or rule> <task id or rule id> --safe-check
```

--safe-check is an optional flag used to list the task or rule that is going to be deleted (and its associated rules in case of task deletion).

### update a task:
```
remove task <task id> --task-name <here the details of the task> --task-status <here specify the new task status>
```

at least one of the two arguments `--task-name` or `--task-status` should be used.