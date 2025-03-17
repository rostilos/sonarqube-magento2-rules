<?php

namespace Magento\Module;

class TestClass
{
    private $userRepository;
    private $logger;

    public function __construct(UserRepository $userRepository, Logger $logger)
    {
        $this->userRepository = $userRepository;
        $this->logger = $logger;

        if ($this->userRepository->getUserCount() > 100) { // Noncompliant {{Constructor limited to dependency assignment and validation.}}
            $this->logger->log('Too many users');
        }
    }
}