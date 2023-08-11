package shop.woowasap.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.auth.controller.request.SignUpRequest;
import shop.woowasap.auth.domain.exception.AuthDomainBaseException;
import shop.woowasap.auth.domain.exception.DuplicatedUserIdException;
import shop.woowasap.auth.domain.in.UserUseCase;
import shop.woowasap.auth.domain.in.request.UserCreateRequest;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserUseCase userUseCase;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {

        userUseCase.createUser(
            new UserCreateRequest(signUpRequest.userId(), signUpRequest.password()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(DuplicatedUserIdException.class)
    public ResponseEntity<String> handleAuthExceptions(
        DuplicatedUserIdException duplicatedUserIdException) {

        log.info(duplicatedUserIdException.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(duplicatedUserIdException.getMessage());
    }

    @ExceptionHandler(AuthDomainBaseException.class)
    public ResponseEntity<String> handleAuthExceptions(
        AuthDomainBaseException authDomainBaseException) {

        log.info(authDomainBaseException.getMessage());
        return ResponseEntity.badRequest().body(authDomainBaseException.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException methodArgumentNotValidException) {

        String defaultMessage = methodArgumentNotValidException
            .getBindingResult()
            .getAllErrors()
            .stream()
            .findFirst()
            .get()
            .getDefaultMessage();
        log.info(defaultMessage);
        return ResponseEntity.badRequest()
            .body(defaultMessage);
    }
}
