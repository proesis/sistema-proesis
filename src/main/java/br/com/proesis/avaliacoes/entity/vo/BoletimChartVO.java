package br.com.proesis.avaliacoes.entity.vo;

/**
 *
 * @author Danilo Souza Almeida
 */
public class BoletimChartVO {
    
    private String category;
    private String series;
    private Float values;

    public BoletimChartVO(String series, String category, Float values) {
        this.category = category;
        this.series = series;
        this.values = values;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShortCategory(){
        StringBuilder sb = new StringBuilder();
        String[] catogorys = category.split(" ");
        for (String c : catogorys) {
            if(c != null && c.length() > 2){
                sb.append(c.substring(0, 3));
                sb.append(" ");
            }
        }
        
        return sb.toString();
    }
    
    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Float getValues() {
        return values;
    }

    public void setValues(Float values) {
        this.values = values;
    }
    
}
