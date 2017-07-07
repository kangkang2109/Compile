package compileTool;

public class Error {
    int row;

    String content;

    public Error(int row, String content) {
        // TODO Auto-generated constructor stub
        this.row = row;
        this.content = content;
    }

    public String[] getErrorData() {
        return new String[] { Integer.toString(getRow()), content };
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return row + "\t" + content;
    }
}
