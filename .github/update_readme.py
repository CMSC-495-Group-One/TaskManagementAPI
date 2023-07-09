import re
import os

directory_path = "src/main/java/com/group1/taskmanagement/controller/"
readme_path = "README.md"

# These are the annotations that define endpoints
endpoint_annotations = {
    "@GetMapping": "GET",
    "@PostMapping": "POST",
    "@PutMapping": "PUT",
    "@DeleteMapping": "DELETE",
    "@PatchMapping": "PATCH"
}

def find_endpoints_in_file(file_path):
    with open(file_path, 'r') as file:
        contents = file.read()

        # Extract class name from file name
        class_name = os.path.splitext(os.path.basename(file_path))[0]

        # Look for class level RequestMapping, capture everything after '${app.base}'
        class_level_mapping = re.search('@RequestMapping\\("\\$\\{app\\.base\\}(.*?)"\\)', contents)
        prefix = class_level_mapping.group(1) if class_level_mapping else ""

        endpoints = []
        for annotation, method in endpoint_annotations.items():
            # Look for method level annotations with optional path
            for match in re.finditer(f'{annotation}(\\("(.*?)"\\))?', contents):
                # If a path is specified, use it, else fall back to class level RequestMapping path
                path = match.group(2) if match.group(2) else prefix
                endpoints.append((method, path))

        return (class_name, endpoints)

def find_endpoints_in_directory(directory_path):
    endpoints_dict = {}
    for root, _, files in os.walk(directory_path):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                class_name, endpoints = find_endpoints_in_file(file_path)
                endpoints_dict[class_name] = endpoints
    return endpoints_dict

def generate_endpoints_markdown(endpoints_dict):
    endpoints_md = ""
    for class_name, endpoints in endpoints_dict.items():
        endpoints_md += f"### {class_name}\n\n"
        for method, endpoint in endpoints:
            endpoints_md += f"```bash\n"
            endpoints_md += f" {method} {endpoint}\n"
            endpoints_md += f"```\n"
    return endpoints_md

def update_readme_with_endpoints(readme_path, endpoints_md):
    with open(readme_path, 'r') as file:
        contents = file.read()

    start_marker = "<!-- ENDPOINTS_START -->"
    end_marker = "<!-- ENDPOINTS_END -->"
    start_index = contents.find(start_marker) + len(start_marker)
    end_index = contents.find(end_marker)

    if start_index == -1 or end_index == -1:
        raise Exception("Could not find the endpoints section in the README.md")

    new_contents = contents[:start_index] + "\n" + endpoints_md + "\n" + contents[end_index:]

    with open(readme_path, 'w') as file:
        file.write(new_contents)

endpoints_dict = find_endpoints_in_directory(directory_path)
endpoints_md = generate_endpoints_markdown(endpoints_dict)
update_readme_with_endpoints(readme_path, endpoints_md)
