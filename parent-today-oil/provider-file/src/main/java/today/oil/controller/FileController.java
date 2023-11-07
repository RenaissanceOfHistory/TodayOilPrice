package today.oil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import today.oil.service.FileService;

/**
 * @create: 2023/11/4
 * @Description:
 * @FileName: FileController
 */
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/file-avatar/upload")
    public String uploadAvatar(@RequestBody String avatar,
                               @RequestParam String filename) {
        return fileService.uploadAvatar(avatar, filename);
    }

    @GetMapping("/file-avatar/download")
    public byte[] downloadAvatar(@RequestParam String filename) {
        return fileService.downloadAvatar(filename);
    }
}
