package models;

public class GroupCreationDTO {
        private String name;
        private String description;
        private boolean open;

        
        public GroupCreationDTO() {
        	
        }

        public String getName() {
            return name;
        }


        public String getDescription() {
            return description;
        }

        public boolean isOpen() {
            return open;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }
    
}
