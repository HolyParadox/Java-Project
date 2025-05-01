

## 🕵️‍♀️ GDPR Compliance Analyzer (Java)

This project is a **Java-based GDPR compliance scanner** that checks websites for legal conformity with data privacy regulations. Originally built between 2016–2019, it was part of a complete toolchain including PHP-based crawling and GUI integration. It analyzes SSL validity, HTTPS redirection, cookies, privacy policies, analytics usage, and more — all through modular functions.

### 🌍 What It Does

- ✅ Validates SSL certificates and HTTPS enforcement  
- 🔍 Checks for missing or weak Impressum and Datenschutzerklärung  
- 🍪 Extracts cookies and inspects their security status  
- 📊 Detects usage of analytics scripts (e.g. Google Analytics, Facebook Pixel)  
- 🕵️‍♂️ Verifies presence of anonymized IP tracking  
- 📑 Parses HTML and flags mixed content  
- 🧩 Checks if analytics and cookies are mentioned in the privacy policy  
- 🌐 Classifies website type (e.g. doctor, lawyer, shop) based on content  
- 📂 Saves results in CSV, PDF, screenshot, and log formats

### 🛠 Tech Stack

- **Java** (Core Logic)
- **Selenium** (for cookie extraction via headless Chrome)
- **Custom Parsers** (regex-based analysis)
- **PhantomJS + PHP** (in companion repo)
- **Legacy Deployment** on offshore VPS nodes

### 🧠 Why It Matters

Built by an autistic programmer with a love for deep systems thinking, this tool reflects an obsession with **data privacy**, **legal structure**, and **ethical technology**. It was used to quietly audit hundreds of German websites for GDPR readiness — long before mainstream tools existed.


⚠️ Disclaimer
❗ This tool is intended for educational and internal testing purposes only.
Do not use it to crawl third-party websites without permission.
Unauthorized crawling can violate Terms of Service or GDPR regulations.
If you wish to use this tool commercially or at scale, implement proper throttling, respect robots.txt, and consult legal guidance.

 



 
