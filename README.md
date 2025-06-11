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
- Clean command for bulk deletions:
    - Delete all tasks with a specific status (e.g. all `Done` tasks)
    - Delete all rules associated with a specific task
    - Delete all rules that have already been satisfied
- List violated date rules that can no longer be satisfied (e.g. missed deadlines)


## 📌 Notes

***TaskTrackCLI*** is terminal-first and designed for fast, rule-based task management. Useful for developers, teams, or anyone managing dependent workflows from the command line.