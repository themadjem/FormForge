# FormForge

FormForge is a cross-platform form generation toolkit designed for flexible, professional-grade PDF form creation.

It works by combining a **master PDF template** with a **job file** that defines:
- What data should be placed onto the form
- Where that data should go
- Which fonts, sizes, and styles to use for each field

FormForge can generate a single form at a time, or batch multiple forms in sequence, depending on the supplied job file.

---

## Project Goals

- 🛠️ **Flexible Layout**: Ability to define any field, anywhere, with precise control over positioning and style.
- 🔥 **Font Control**: Support custom fonts and font sizes for specific fields (e.g., barcode fonts like IMb).
- 🌎 **Cross-Platform**: Fully functional on Linux and Windows.
- 🧩 **Modular Job Files**: Each form job is driven by an external job file (JSON format) that defines all necessary data and layout settings.
- ☁️ **Web-Ready Design** (Future): The architecture should make it easy to wrap the backend into a lightweight web application later.

---

## How It Works (Planned)

1. **Prepare a master PDF**:  
   Your base document, with blank spots where data will go.

2. **Create a job file**:  
   JSON file that specifies which fields to fill, their coordinates, font settings, and the master form to use.

3. **Run FormForge**:  
   FormForge reads the master document and the job file, fills in the data as specified, and outputs a completed PDF.

---

## Example Job File

```json
{
  "masterForm": "templates/invoice_template.pdf",
  "outputFile": "output/invoice_1234.pdf",
  "fields": [
    {
      "name": "AutomationBarcode",
      "text": "E311224012345-1",
      "position": { "x": 300, "y": 50, "rotation": 90 },
      "font": { "name": "Free3of9", "size": 14, "format": [] }
    },
    {
      "name": "CustomerName",
      "text": "John Doe",
      "position": { "x": 100, "y": 700 },
      "font": { "name": "Helvetica-Bold", "size": 14, "format": ["underline"] }
    },
    {
      "name": "CustomerAddress",
      "text": "123 Main Street",
      "position": { "x": 100, "y": 680 },
      "font": { "name": "Helvetica", "size": 12 }
    }
  ]
}
```

---

## Project Status

- [X] Project planning
- [ ] Base application skeleton
- [ ] Job file reader
- [ ] PDF master loader
- [ ] Field placer and font controller
- [ ] Output writer
- [ ] CLI runner
- [ ] (Optional) Web UI prototype


---

## Notes

    Would like to use Kotlin for clean, modern, cross-platform development.

    Powered by open-source PDF libraries.

    Inspired by real-world production print systems and industrial form generation workflows.

---
