package com.sample.batch.jobs.user;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.sample.batch.context.BatchContext;
import com.sample.batch.context.BatchContextHolder;
import com.sample.batch.item.ItemError;
import com.sample.batch.jobs.BaseTasklet;
import com.sample.common.util.MessageUtils;
import com.sample.domain.dao.users.UserDao;
import com.sample.domain.dto.user.User;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * ユーザー情報取り込みタスク
 */
@Slf4j
public class ImportUserTasklet extends BaseTasklet<ImportUserDto> {

    @Autowired
    UserDao userDao;

    @Autowired
    ImportUserValidator importUserValidator;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws IOException {
        val status = super.execute(contribution, chunkContext);

        val context = BatchContextHolder.getContext();
        val errors = getErrors(context);

        if (isNotEmpty(errors)) {
            errors.forEach(e -> {
                val sourceName = e.getSourceName();
                val position = e.getPosition();
                val errorMessage = e.getErrorMessage();
                log.error("エラーがあります。ファイル名={}, 行数={}, エラーメッセージ={}", sourceName, position, errorMessage);
            });

            throw new ValidationException("取り込むファイルに不正な行が含まれています。");
        }

        return status;
    }

    @Override
    protected List<Stream<ImportUserDto>> doRead(BatchContext context) throws IOException {
        Path path1 = Paths.get("src/main/resources/sample_users.csv");
        Stream<ImportUserDto> stream1 = Files.lines(path1).skip(1).map(line -> {
            val row = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ",");
            val dto = new ImportUserDto();
            dto.setSourceName(path1.toString());
            dto.setFirstName(row[0]);
            dto.setLastName(row[1]);
            dto.setEmail(row[2]);
            dto.setTel(row[3]);
            dto.setZip(row[4]);
            dto.setAddress(row[5]);
            return dto;
        });

        Path path2 = Paths.get("src/main/resources/sample_users2.csv");
        Stream<ImportUserDto> stream2 = Files.lines(path2).skip(1).map(line -> {
            val row = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ",");
            val dto = new ImportUserDto();
            dto.setSourceName(path2.toString());
            dto.setFirstName(row[0]);
            dto.setLastName(row[1]);
            dto.setEmail(row[2]);
            dto.setTel(row[3]);
            dto.setZip(row[4]);
            dto.setAddress(row[5]);
            return dto;
        });

        return Arrays.asList(stream1, stream2);
    }

    @Override
    protected void onValidationError(BatchContext context, BindingResult br, ImportUserDto item) {
        br.getFieldErrors().forEach(e -> {
            val errorMessage = MessageUtils.getMessage(e);

            val error = new ItemError();
            error.setSourceName(item.getSourceName());
            error.setPosition(item.getPosition());
            error.setErrorMessage(errorMessage);

            addErrors(context, error);
        });
    }

    @Override
    protected void doProcess(BatchContext context, ImportUserDto item) {
        val user = modelMapper.map(item, User.class);
        userDao.insert(user);
    }

    @Override
    protected Validator getValidator() {
        return importUserValidator;
    }

    @SuppressWarnings("unchecked")
    private List<ItemError> getErrors(BatchContext context) {
        val additionalInfo = context.getAdditionalInfo();
        List<ItemError> errors = (List<ItemError>) additionalInfo.get("errors");

        if (errors == null) {
            errors = new ArrayList<>();
        }

        return errors;
    }

    @SuppressWarnings("unchecked")
    private void addErrors(BatchContext context, ItemError... itemErrors) {
        val additionalInfo = context.getAdditionalInfo();
        List<ItemError> errors = (List<ItemError>) additionalInfo.get("errors");

        if (errors == null) {
            errors = new ArrayList<>();
        }

        if (itemErrors != null) {
            errors.addAll(Arrays.asList(itemErrors));
        }

        additionalInfo.put("errors", errors);
    }
}
