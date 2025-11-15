# FairSort Registration System

Simple Java GUI demo that uses a Queue (LinkedList) to manage a first-come, first-served student registration line.

## Project structure

- `src/` - Java source files (`App.java`, `Student.java`)
- `bin/` - compiled class files (created by `javac -d bin ...`)

## Build

From the project root (where `src` is located), run:

```powershell
javac -d bin -sourcepath src src\\*.java
```

This compiles the sources into the `bin` directory.

## Run

Run the GUI application:

```powershell
java -cp bin App
```

This opens a Swing window where you can:
- Add Student to Queue: enter Name and ID, then click the button to enqueue.
- Process Next Student: removes the student at the front of the queue and marks them as served.
- View Current Queue: shows the list of students currently waiting (front -> back).

Additional controls added:
- Add Sample Data: enqueues three sample students (Maria Garcia, John Doe, Alice Albright) to demonstrate FIFO behavior.
- Clear Queue: removes all entries from the current in-memory queue.
- Save Queue: saves the current queue to `queue_data.txt` in the project root (simple pipe-separated format: name|id|arrivalMillis).
- Load Queue: loads entries from `queue_data.txt` and appends them to the current queue.
- Export CSV: writes `queue_export.csv` with a header `Name,ID,Arrival` and one row per queued student.

## Sample Walkthrough

Initial State: Queue is empty

1. Enqueue Maria Garcia, 24-00042
2. Enqueue John Doe, 24-00155
3. Enqueue Alice Albright, 24-00001

Queue (front -> back): Maria Garcia -> John Doe -> Alice Albright

Process Next Student -> Now serving: Maria Garcia, 24-00042
Process Next Student -> Now serving: John Doe, 24-00155

This demonstrates FIFO fairness: order of arrival is preserved regardless of names or ID numbers.

## Notes

- The GUI uses `LinkedList` as a `Queue<Student>` implementation.
- If you want a command-line demo or unit tests added, I can add them next.
## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
