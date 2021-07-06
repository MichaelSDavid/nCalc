// (Private outside package)
@SuppressWarnings("FieldMayBeFinal")
class Expression {
    // Field(s) init
    private int pos = -1, chr;
    private String str;

    @SuppressWarnings("RegExpUnexpectedAnchor")
    public Expression(String str) {
        // Replace all constants with their numerical values (including a correction for sec(x))
        str = str.toLowerCase().contains("pi") ? str.replaceAll("pi", "(3.141592653589793)") : str;
        str = str.toLowerCase().contains("e") ? str.replaceAll("e", "(2.718281828459045)") : str;
        str = str.toLowerCase().contains("s(2.718281828459045)c") ? str.replaceAll("s\\(2\\.718281828459045\\)c", "sec") : str;
        this.str = str;
    }

    // Traverse to next char
    private void nextChar() {
        if (++pos < str.length()) {
            chr = str.charAt(pos);
        } else {
            chr = -1;
        }
    }

    // Scan the current char
    private boolean scanChar(int finalChar) {
        // Ignore spaces
        while (chr == ' ') {
            nextChar();
        }

        if (chr == finalChar) {
            nextChar();
            return true;
        }
        return false;
    }

    // Parse the expression (overall)
    private double parse() {
        nextChar();

        // Parse (each) term
        double x = parseTerm();

        if (pos < str.length()) {
            // When reaching an unexpected character
            throw new RuntimeException("Unexpected character: " + (char) chr);
        }
        return x;
    }

    // Parse single term
    private double parseTerm() {
        // Parse group within term
        double x = parseGroup();

        // + or - for lowest priority
        while (true) {
            if (scanChar('+')) {
                // Addition present within term
                x += parseGroup();
            } else if (scanChar('-')) {
                // Subtraction present within term
                x -= parseGroup();
            } else {
                // Break once all groups have been accounted for
                return x;
            }
        }
    }

    private double parseGroup() {
        // Parse chunk (bracketed) within group
        double x = parseChunk();

        // '*' or '/' greater priority than '+' or '-'
        while (true) {
            if (scanChar('*')) {
                // Multiplication present within group
                x *= parseChunk();
            } else if (scanChar('/')) {
                // Division present within group
                x /= parseChunk();
            } else {
                return x;
            }
        }
    }

    private double parseChunk() {
        // Account for positive and negative coefficients
        if (scanChar('+')) {
            // Multiplicative (+1)
            return parseChunk();
        }
        if (scanChar('-')) {
            // Multiplicative (-1)
            return (-1) * parseChunk();
        }

        double x;
        int startPos = this.pos;

        if (scanChar('(')) {
            // Brackets (highest priority)
            x = parseTerm();
            scanChar(')');
        } else if ((chr >= '0' && chr <= '9') || chr == '.') {
            while ((chr >= '0' && chr <= '9') || chr == '.') {
                // Single numbers
                nextChar();
            }
            x = Double.parseDouble(str.substring(startPos, this.pos));
        } else if (chr >= 'a' && chr <= 'z') {
            while (chr >= 'a' && chr <= 'z') {
                // Parse special functions
                nextChar();
            }
            String func = str.substring(startPos, this.pos);
            x = parseChunk();

            // Cases for each special function
            switch (func) {
                case "sqrt":
                    x = Math.sqrt(x);
                    break;
                case "cbrt":
                    x = Math.pow(x, 1.0/3);
                    break;
                case "sin":
                    x = Math.sin(x);
                    break;
                case "cos":
                    x = Math.cos(x);
                    break;
                case "tan":
                    x = Math.tan(x);
                    break;
                case "csc":
                    x = 1 / Math.sin(x);
                    break;
                case "sec":
                    x = 1 / Math.cos(x);
                    break;
                case "cot":
                    x = 1 / Math.tan(x);
                    break;
                case "ln":
                    x = Math.log(x);
                    break;
                case "log":
                    x = Math.log10(x);
                    break;
                default:
                    // When reaching an unexpected function
                    throw new RuntimeException("Unknown function: " + func);
            }
        } else {
            throw new RuntimeException("Unexpected character: " + (char) chr);
        }

        // '^' greater priority than '*' or '/'
        if (scanChar('^')) {
            x = Math.pow(x, parseChunk());
        }
        return x;
    }

    public double evaluate() {
        // Operate method on object from main parse()
        return this.parse();
    }
}
