package shop.woowasap.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.auth.domain.exception.AuthDomainBaseException;
import shop.woowasap.auth.domain.exception.DuplicatedUserIdException;
import shop.woowasap.auth.domain.in.UserUseCase;
import shop.woowasap.auth.domain.in.request.UserCreateRequest;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserUseCase userUseCase;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody UserCreateRequest userCreateRequest) {
        userUseCase.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(DuplicatedUserIdException.class)
    public ResponseEntity<String> handleAuthExceptions(DuplicatedUserIdException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(AuthDomainBaseException.class)
    public ResponseEntity<String> handleAuthExceptions(AuthDomainBaseException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
