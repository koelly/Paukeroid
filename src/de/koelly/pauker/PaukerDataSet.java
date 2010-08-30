package de.koelly.pauker;

public class PaukerDataSet {
	
	private String lesson = null;
	private String description = null;
	private String frontText = null;
	private String backText = null;

	public PaukerDataSet(String _lesson, String _description, String _frontText, String _backText){
		this.lesson = _lesson;
		this.description = _description;
		this.frontText = _frontText;
		this.backText = _backText;
		}
 
        public PaukerDataSet() {
        }

		public String getLesson() {
                return lesson;
        }
        public void setLesson(String _lesson) {
                this.lesson = _lesson;
        }
        
        public String getDescription() {
            return description;
        }
        public void setDescription(String _description) {
            this.description = _description;
        }
        
        public String getFrontText() {
            return frontText;
        }
        public void setFrontText(String _frontText) {
            this.frontText = _frontText;
        }
        
        public String getBackText() {
            return backText;
        }
        public void setBackText(String _backText) {
            this.backText = _backText;
        }

}
