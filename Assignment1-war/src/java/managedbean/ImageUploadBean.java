package managedbean;

import exception.AccountNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.primefaces.model.file.UploadedFile;
import session.AccountSessionLocal;

/**
 *
 * @author jayso
 */
@Named(value = "imageUploadBean")
@RequestScoped
public class ImageUploadBean {

    @EJB
    private AccountSessionLocal accountSessionLocal;

    @Inject
    private AuthenticationManagedBean authenticationManagedBean;

    private UploadedFile uploadedfile;
    private String filename = "";
    private boolean showUploadForm = false;

    public ImageUploadBean() {
    }

    public void upload() throws IOException, AccountNotFoundException {
        if (getUploadedfile() != null) {
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String UPLOAD_DIRECTORY = ctx.getRealPath("/") + "upload/";
            System.out.println("#UPLOAD_DIRECTORY : " + UPLOAD_DIRECTORY);

            // Use getFileName from UploadedFile
            setFilename(getUploadedfile().getFileName());
            accountSessionLocal.putProfilePicture(authenticationManagedBean.getUserId(), getFilename());
            System.out.println("filename: " + getFilename());

            //replace existing file
            Path path = Paths.get(UPLOAD_DIRECTORY + getFilename());
            InputStream bytes = getUploadedfile().getInputStream();
            Files.copy(bytes, path, StandardCopyOption.REPLACE_EXISTING);

            FacesMessage message = new FacesMessage("Successful", getUploadedfile().getFileName() + " Profile picture uploaded");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage("Error", "An error occurred during file upload.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

//    public void upload() throws IOException, AccountNotFoundException {
//        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//
//        //get the deployment path
//        String UPLOAD_DIRECTORY = ctx.getRealPath("/") + "upload/";
//        System.out.println("#UPLOAD_DIRECTORY : " + UPLOAD_DIRECTORY);
//
//        //debug purposes
//        setFilename(Paths.get(uploadedfile.getSubmittedFileName()).getFileName().toString());
//        accountSessionLocal.putProfilePicture(authenticationManagedBean.getUserId(), getFilename());
//        System.out.println("filename: " + getFilename());
//        //---------------------
//
//        //replace existing file
//        Path path = Paths.get(UPLOAD_DIRECTORY + getFilename());
//        InputStream bytes = uploadedfile.getInputStream();
//        Files.copy(bytes, path, StandardCopyOption.REPLACE_EXISTING);
//      
//    }
//    public Part getUploadedfile() {
//        return uploadedfile;
//    }
//
//    public void setUploadedfile(Part uploadedfile) {
//        this.uploadedfile = uploadedfile;
//    }

    public UploadedFile getUploadedfile() {
        return uploadedfile;
    }

    public void setUploadedfile(UploadedFile uploadedfile) {
        this.uploadedfile = uploadedfile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isShowUploadForm() {
        return showUploadForm;
    }

    public void setShowUploadForm(boolean showUploadForm) {
        this.showUploadForm = showUploadForm;
    }

    // Toggle visibility
    public void toggleUploadForm() {
        this.showUploadForm = !this.showUploadForm;
    }

}
